package com.example.weaponMaster.modules.comment.service;

import com.example.weaponMaster.api.comments.dto.ReqCommentsDto;
import com.example.weaponMaster.modules.account.constant.LogActType;
import com.example.weaponMaster.modules.account.constant.LogContentsType;
import com.example.weaponMaster.modules.account.constant.UserType;
import com.example.weaponMaster.modules.account.entity.UserInfo;
import com.example.weaponMaster.modules.account.service.UserInfoService;
import com.example.weaponMaster.modules.account.service.UserLogService;
import com.example.weaponMaster.modules.article.constant.ArticleDetailType;
import com.example.weaponMaster.modules.article.constant.ArticleType;
import com.example.weaponMaster.modules.article.constant.CategoryType;
import com.example.weaponMaster.modules.article.dto.ArticleDto;
import com.example.weaponMaster.modules.article.entity.Article;
import com.example.weaponMaster.modules.article.service.ArticleService;
import com.example.weaponMaster.modules.comment.dto.CommentDto;
import com.example.weaponMaster.modules.comment.entity.Comment;
import com.example.weaponMaster.modules.comment.repository.CommentRepository;
import com.example.weaponMaster.modules.common.constant.MyURL;
import com.example.weaponMaster.modules.common.dto.ApiResponse;
import com.example.weaponMaster.modules.slack.constant.AdminSlackChannelType;
import com.example.weaponMaster.modules.slack.service.SlackService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final ArticleService    articleService;
    private final UserInfoService   userInfoService;
    private final CommentRepository commentRepository;
    private final UserLogService    userLogService;
    private final SlackService      slackService;
    
    @Transactional
    public ApiResponse<Void> createComment(ReqCommentsDto request) {
        ArticleDto article = articleService.getArticle(request.getArticleId()).getData();
        if (article == null) {
            throw new IllegalArgumentException(String.format("[댓글 등록 에러] 게시글 정보를 확인할 수 없습니다. userId: %s, articleID: %d", request.getUserId(), request.getArticleId()));
        }

        // 공지사항/업데이트 게시글에는 댓글 기재 불가
        if (isNewsAndNotCommentable(article)) {
            throw new IllegalArgumentException(String.format("[댓글 등록 에러] 공지사항/업데이트 게시글에 댓글 등록 시도 userId: %s, articleID: %d, categoryType: %d", request.getUserId(), request.getArticleId(), article.getCategoryType()));
        }

        // 1:1 문의 게시글인 경우
        if (isPrivateContact(article)) {
            if (isAdminUser(request)) {
                Comment savedComment = saveComment(request);
                updateArticleIfFirstReply(request);
                userLogService.saveLog(request.getUserId(), request.getIsAdmin(), LogContentsType.ARTICLE, LogActType.CREATE_COMMENT, (short)(int)request.getArticleId(), (short)(int)savedComment.getId());
                return ApiResponse.success();
            }

            if (isArticleOwner(request, article)) {
                Comment savedComment = saveComment(request);
                userLogService.saveLog(request.getUserId(), request.getIsAdmin(), LogContentsType.ARTICLE, LogActType.CREATE_COMMENT, (short)(int)request.getArticleId(), (short)(int)savedComment.getId());
                slackService.sendMessageAdmin(AdminSlackChannelType.PRIVATE_CONTACT_NOTICE, getNoticeMessage(article, savedComment));
                return ApiResponse.success();
            }

            // 관리자도 아니고 소유자도 아닌 경우
            throw new IllegalArgumentException(String.format("[1:1문의 댓글 등록 에러] 관리자/소유자가 아니지만 댓글 등록 시도 userId: %s, articleID: %d, author: %s", request.getUserId(), request.getArticleId(), article.getUserId()));
        }

        // 일반 게시글의 경우
        Comment savedComment = saveComment(request);
        userLogService.saveLog(request.getUserId(), request.getIsAdmin(), LogContentsType.ARTICLE, LogActType.CREATE_COMMENT, (short)(int)request.getArticleId(), (short)(int)savedComment.getId());

        return ApiResponse.success();
    }

    // TODO -> 1:1 게시물 쪽 함수 getNoticeMessage() 랑 공통화 하기
    private String getNoticeMessage(ArticleDto userArticle, Comment userComment) {
        // 1. HTML 태그 제거
        String plainText = userComment.getContents()
                .replaceAll("(?i)<br\\s*/?>", "\n")             // <br>, <br/> → 줄바꿈
                .replaceAll("(?i)</p>", "\n")                   // </p> → 줄바꿈
                .replaceAll("(?i)</h[1-6]>", "\n")              // 제목 태그 종료 → 줄바꿈
                .replaceAll("(?i)</(em|strong|span)>", " ")     // 강조, span 태그 종료 → 공백으로 대체
                .replaceAll("(?i)<[^>]*>", "")                  // 나머지 HTML 태그 제거
                .replaceAll("&nbsp;", " ")                      // &nbsp; → 일반 공백
                .replaceAll("[ \\t]+", " ")                     // 연속된 공백 정리
                .replaceAll("(?m)^\\s+", "")                    // 각 줄 시작의 공백 제거
                .replaceAll("(?m)\\s+\n", "\n")                 // 줄 끝의 불필요한 공백 제거
                .trim();

        // 2. 길이 제한
        int maxLength = 80;
        if (plainText.length() > maxLength) {
            plainText = plainText.substring(0, maxLength) + "...";
        }

        // 이모지코드: 💬
        String link = String.format("%s/service/%d", MyURL.WEAPON_MASTER, userArticle.getId());
        String message = String.format(
                "`\uD83D\uDCAC 1:1 문의 댓글 등록` - <%s|링크 바로가기>\n" +
                        "```" +
                        "제목: %s\n" +
                        "이름: %s\n" +
                        "댓글: %s" +
                        "```",
                link,
                userArticle.getTitle(),
                userArticle.getUserId(),
                plainText
        );
        return message;
    }

    private boolean isNewsAndNotCommentable(ArticleDto article) {
        if (article.getCategoryType() == CategoryType.NEWS) {
            if (article.getArticleType() == ArticleType.NEWS.NOTICE || article.getArticleType() == ArticleType.NEWS.UPDATE) {
                return true;
            }
        }

        return false;
    }

    private boolean isPrivateContact(ArticleDto article) {
        if (article.getCategoryType() == CategoryType.SERVICE_CENTER) {
            if (article.getArticleType() == ArticleType.SERVICE_CENTER.PRIVATE_CONTACT) {
                return true;
            }
        }

        return false;
    }

    private boolean isAdminUser(ReqCommentsDto request) {
        if (!request.getIsAdmin()) return false;

        UserInfo userInfo = userInfoService.getUserInfoEntity(request.getUserId());
        if (userInfo.getUserType() == UserType.ADMIN) {
            return true;
        }

        return false;
    }

    private boolean isArticleOwner(ReqCommentsDto request, ArticleDto article) {
        if (Objects.equals(request.getUserId(), article.getUserId())) {
            return true;
        }

        return false;
    }

    private Comment saveComment(ReqCommentsDto request) {
        Comment comment = new Comment(
                request.getUserId(),
                request.getArticleId(),
                request.getReCommentId(),
                request.getContents()
        );

        Comment savedComment = commentRepository.save(comment);
        updateArticleCommentCount(request.getArticleId());
        return savedComment;
    }

    private void updateArticleIfFirstReply(ReqCommentsDto request) {
        // saveComment() 이후 최신 정보로 다시 조회
        ArticleDto article = articleService.getArticle(request.getArticleId()).getData();

        if (article.getArticleDetailType() == ArticleDetailType.SERVICE_CENTER.PRIVATE_CONTACT.WAITING) {
            article.setArticleDetailType(ArticleDetailType.SERVICE_CENTER.PRIVATE_CONTACT.ANSWERED);
            articleService.updateArticleDto(article);
        }
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
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(String.format("[댓글 삭제 에러] 삭제하려는 댓글 정보를 확인할 수 없습니다. userId: %s, comment ID: %d", request.getUserId(), id)));

        // 1. 댓글 소유자가 맞는지 확인
        if (!comment.getUserId().equals(request.getUserId())) {
            UserInfo userInfo = userInfoService.getUserInfoEntity(request.getUserId());
            if (userInfo == null) {
                throw new IllegalArgumentException("[댓글 삭제 에러] User not found, userId: " + request.getUserId());
            }

            // 2. 소유자가 다를 경우 관리자 권한 있는지 확인
            if (!request.getIsAdmin()) {
                throw new IllegalArgumentException(String.format("[댓글 삭제 에러] 관리자모드가 아닌 상태에서 삭제 시도 userId: %s, comment ID: %d, commentUserId: %s" , request.getUserId(), id, comment.getUserId()));
            }
            if (userInfo.getUserType() != UserType.ADMIN) {
                throw new IllegalArgumentException(String.format("[댓글 삭제 에러] User does not have admin privileges. userId: %s, comment ID: %d", request.getUserId(), id));
            }
        }

        comment.setIsDeleted(true);
        Comment savedComment = commentRepository.save(comment);
        userLogService.saveLog(request.getUserId(), request.getIsAdmin(), LogContentsType.ARTICLE, LogActType.DELETE_COMMENT, (short)(int)request.getArticleId(), (short)(int)savedComment.getId());

        return ApiResponse.success();
    }

    private void updateArticleCommentCount(Integer articleId) {
        int commentCount = commentRepository.countByArticleId(articleId);
        articleService.updateCommentCount(articleId, commentCount);
    }

    private CommentDto convertToDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .userId(comment.getUserId())
                .articleId(comment.getArticleId())
                .reCommentId(comment.getReCommentId())
                .contents(comment.getContents())
                .isDeleted(comment.getIsDeleted())
                .createDate(comment.getCreateDate())
                .updateDate(comment.getUpdateDate())
                .build();
    }
}
