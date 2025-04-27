package com.example.weaponMaster.modules.comment.service;

import com.example.weaponMaster.api.comments.dto.ReqCommentsDto;
import com.example.weaponMaster.modules.account.constant.LogActType;
import com.example.weaponMaster.modules.account.constant.LogContentsType;
import com.example.weaponMaster.modules.account.constant.UserType;
import com.example.weaponMaster.modules.account.entity.UserInfo;
import com.example.weaponMaster.modules.account.repository.UserInfoRepository;
import com.example.weaponMaster.modules.account.service.UserLogService;
import com.example.weaponMaster.modules.article.constant.ArticleDetailType;
import com.example.weaponMaster.modules.article.constant.ArticleType;
import com.example.weaponMaster.modules.article.constant.CategoryType;
import com.example.weaponMaster.modules.article.entity.Article;
import com.example.weaponMaster.modules.article.repository.ArticleRepository;
import com.example.weaponMaster.modules.comment.dto.CommentDto;
import com.example.weaponMaster.modules.comment.entity.Comment;
import com.example.weaponMaster.modules.comment.repository.CommentRepository;
import com.example.weaponMaster.modules.common.constant.MyURL;
import com.example.weaponMaster.modules.common.dto.ApiResponse;
import com.example.weaponMaster.modules.slack.constant.AdminSlackChannelType;
import com.example.weaponMaster.modules.slack.constant.UserSlackNoticeType;
import com.example.weaponMaster.modules.slack.service.SlackService;
import com.example.weaponMaster.modules.slack.util.HtmlUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final ArticleRepository  articleRepository;
    private final CommentRepository  commentRepository;
    private final UserInfoRepository userInfoRepository;
    private final UserLogService     userLogService;
    private final SlackService       slackService;

    @Transactional
    public ApiResponse<Void> createComment(ReqCommentsDto request) {
        // 게시글 정보 확인
        Article article = articleRepository.findById(request.getArticleId()).orElse(null);
        if (article == null) {
            throw new IllegalArgumentException(String.format("[댓글 등록 에러] 게시글 정보를 확인할 수 없습니다. userId: %s, articleID: %d", request.getUserId(), request.getArticleId()));
        }

        // 댓글 등록 가능 상태 확인
        validateOrThrow(article, request);

        // 댓글 등록
        Comment savedComment = saveComment(request);
        userLogService.saveLog(request.getUserId(), request.getIsAdmin(), LogContentsType.ARTICLE, LogActType.CREATE_COMMENT, (short) (int) request.getArticleId(), (short) (int) savedComment.getId());

        // 1:1 게시글인 경우
        if (isPrivateContact(article)) {
            handlePrivateContactComment(article, request, savedComment);
            return ApiResponse.success();
        }

        return ApiResponse.success();
    }

    private void validateOrThrow(Article article, ReqCommentsDto request) {
        // 공지사항/업데이트 게시글에는 댓글 기재 불가
        if (isNewsAndNotCommentable(article)) {
            throw new IllegalArgumentException(String.format("[댓글 등록 에러] 공지사항/업데이트 게시글에 댓글 등록 시도 userId: %s, articleID: %d, categoryType: %d", request.getUserId(), request.getArticleId(), article.getCategoryType()));
        }

        // 관리자모드 사용 가능한 유저인지 확인
        if (request.getIsAdmin()) {
            UserInfo userInfo = userInfoRepository.findByUserId(request.getUserId());
            if (userInfo == null) {
                throw new IllegalArgumentException(String.format("[댓글 등록 에러] 사용자의 정보를 조회할 수 없습니다. userId: %s, articleID: %d", request.getUserId(), request.getArticleId()));
            }

            if (userInfo.getUserType() != UserType.ADMIN) {
                throw new IllegalArgumentException(String.format("[댓글 등록 에러] 관리자 권한이 없습니다. userId: %s, userType: %d, articleID: %d", request.getUserId(), userInfo.getUserType(), request.getArticleId()));
            }
        }
    }

    private void handlePrivateContactComment(Article article, ReqCommentsDto request, Comment savedComment) {
        // 1:1 문의 > 관리자의 최초 댓글 기재면 답변 완료 상태로 업데이트 및 유저에게 슬랙 알림 발송
        if (request.getIsAdmin()) {
            if (article.getArticleDetailType() == ArticleDetailType.SERVICE_CENTER.PRIVATE_CONTACT.WAITING) {
                article.setArticleDetailType(ArticleDetailType.SERVICE_CENTER.PRIVATE_CONTACT.ANSWERED);
                articleRepository.save(article);
                slackService.sendMessage(article.getUserId(), UserSlackNoticeType.WEAPON_MASTER_SERVICE_ALERT, getUserNoticeMessage(article, savedComment));
                return;
            }
        }

        // 1:1 문의 > 유저가 추가 댓글을 기재했다면 관리자 슬랙 알림 발송
        if (isArticleOwner(request, article)) {
            slackService.sendMessageAdmin(AdminSlackChannelType.PRIVATE_CONTACT_NOTICE, getAdminNoticeMessage(article, savedComment));
            return;
        }

        throw new IllegalArgumentException(String.format("[1:1문의 댓글 등록 에러] 관리자/소유자가 아니지만 댓글 등록 시도 userId: %s, articleID: %d, author: %s", request.getUserId(), request.getArticleId(), article.getUserId()));
    }

    private Comment saveComment(ReqCommentsDto request) {
        // 댓글 저장
        Comment comment = new Comment(
                request.getUserId(),
                request.getArticleId(),
                request.getReCommentId(),
                request.getContents(),
                request.getIsAdmin()
        );
        Comment savedComment = commentRepository.save(comment);

        // 게시물 정보 확인
        Article article = articleRepository.findById(request.getArticleId()).orElse(null);
        if (article == null) {
            throw new IllegalArgumentException(String.format("[댓글 등록 에러] 게시글 정보를 확인할 수 없습니다. userId: %s, articleID: %d", request.getUserId(), request.getArticleId()));
        }

        // 게시물 댓글 개수 수정
        int commentCount = commentRepository.countByArticleId(request.getArticleId());
        article.setCommentCount(commentCount);
        articleRepository.save(article);

        return savedComment;
    }

    private String getUserNoticeMessage(Article article, Comment userComment) {
        // 1. HTML 태그 제거 및 정리
        String plainText = HtmlUtil.getPlainText(userComment.getContents());

        // 2. 길이 제한
        int maxLength = 80;
        if (plainText.length() > maxLength) {
            plainText = plainText.substring(0, maxLength) + "...";
        }

        // 이모지코드: 💬
        String link = String.format("%s/service/%d", MyURL.WEAPON_MASTER, article.getId());
        String message = String.format(
                "문의 주신 내용에 대한 답변이 완료되었습니다.\n" +
                        "`\uD83D\uDCAC 1:1 문의 답변완료 알림` - <%s|링크 바로가기>\n" +
                        "```" +
                        "제목: %s\n" +
                        "답변: %s" +
                        "```",
                link,
                article.getTitle(),
                plainText
        );
        return message;
    }

    private String getAdminNoticeMessage(Article article, Comment userComment) {
        // 1. HTML 태그 제거 및 정리
        String plainText = HtmlUtil.getPlainText(userComment.getContents());

        // 2. 길이 제한
        int maxLength = 80;
        if (plainText.length() > maxLength) {
            plainText = plainText.substring(0, maxLength) + "...";
        }

        // 이모지코드: 💬
        String link = String.format("%s/service/%d", MyURL.WEAPON_MASTER, article.getId());
        String message = String.format(
                "`\uD83D\uDCAC 1:1 문의 댓글 등록` - <%s|링크 바로가기>\n" +
                        "```" +
                        "제목: %s\n" +
                        "이름: %s\n" +
                        "댓글: %s" +
                        "```",
                link,
                article.getTitle(),
                article.getUserId(),
                plainText
        );
        return message;
    }

    private boolean isNewsAndNotCommentable(Article article) {
        if (article.getCategoryType() == CategoryType.NEWS) {
            if (article.getArticleType() == ArticleType.NEWS.NOTICE || article.getArticleType() == ArticleType.NEWS.UPDATE) {
                return true;
            }
        }

        return false;
    }

    private boolean isPrivateContact(Article article) {
        if (article.getCategoryType() == CategoryType.SERVICE_CENTER) {
            if (article.getArticleType() == ArticleType.SERVICE_CENTER.PRIVATE_CONTACT) {
                return true;
            }
        }

        return false;
    }

    private boolean isArticleOwner(ReqCommentsDto request, Article article) {
        if (Objects.equals(request.getUserId(), article.getUserId())) {
            return true;
        }

        return false;
    }

    @Transactional
    public ApiResponse<CommentDto[]> getCommentList(Integer articleId) {
        Comment[] comments = commentRepository.findByArticleId(articleId);
        if (comments == null || comments.length == 0) {
            return ApiResponse.success(new CommentDto[0]);
        }

        CommentDto[] commentDtoList = Arrays.stream(comments)
                .map(this::convertToDto)
                .toArray(CommentDto[]::new);

        return ApiResponse.success(commentDtoList);
    }

    @Transactional
    public ApiResponse<Void> deleteComment(ReqCommentsDto request, Integer id) {
        Comment comment = commentRepository.findById(id).orElse(null);
        if (comment == null) {
            throw new IllegalArgumentException(String.format("[댓글 삭제 에러] 삭제하려는 댓글 정보를 확인할 수 없습니다. userId: %s, comment ID: %d", request.getUserId(), id));
        }

        // 1. 댓글 소유자가 맞는지 확인
        if (!comment.getUserId().equals(request.getUserId())) {
            UserInfo userInfo = userInfoRepository.findByUserId(request.getUserId());
            if (userInfo == null) {
                throw new IllegalArgumentException("[댓글 삭제 에러] User not found, userId: " + request.getUserId());
            }

            // 2. 소유자가 다를 경우 관리자 권한 있는지 확인
            if (!request.getIsAdmin() || userInfo.getUserType() != UserType.ADMIN) {
                throw new IllegalArgumentException(String.format("[댓글 삭제 에러] 권한 없음 userId: %s, comment ID: %d", request.getUserId(), id));
            }
        }

        // 3. 댓글 삭제 상태로 업데이트
        comment.setIsDeleted(true);
        Comment savedComment = commentRepository.save(comment);
        userLogService.saveLog(request.getUserId(), request.getIsAdmin(), LogContentsType.ARTICLE, LogActType.DELETE_COMMENT, (short)(int)request.getArticleId(), (short)(int)savedComment.getId());

        return ApiResponse.success();
    }

    private CommentDto convertToDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .userId(comment.getUserId())
                .articleId(comment.getArticleId())
                .reCommentId(comment.getReCommentId())
                .contents(comment.getContents())
                .isDeleted(comment.getIsDeleted())
                .isAdminMode(comment.getIsAdminMode())
                .createDate(comment.getCreateDate())
                .updateDate(comment.getUpdateDate())
                .build();
    }
}

