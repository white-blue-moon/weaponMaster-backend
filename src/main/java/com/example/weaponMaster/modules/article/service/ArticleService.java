package com.example.weaponMaster.modules.article.service;

import com.example.weaponMaster.api.articles.dto.ReqArticlesDto;
import com.example.weaponMaster.modules.account.constant.LogActType;
import com.example.weaponMaster.modules.account.constant.LogContentsType;
import com.example.weaponMaster.modules.account.constant.UserType;
import com.example.weaponMaster.modules.account.entity.UserInfo;
import com.example.weaponMaster.modules.account.repository.UserInfoRepository;
import com.example.weaponMaster.modules.account.service.UserLogService;
import com.example.weaponMaster.modules.account.service.UserPermissionService;
import com.example.weaponMaster.modules.article.constant.ArticleType;
import com.example.weaponMaster.modules.article.constant.CategoryType;
import com.example.weaponMaster.modules.article.dto.ArticleDto;
import com.example.weaponMaster.modules.article.entity.Article;
import com.example.weaponMaster.modules.article.repository.ArticleRepository;
import com.example.weaponMaster.modules.comment.repository.CommentRepository;
import com.example.weaponMaster.modules.common.constant.MyURL;
import com.example.weaponMaster.modules.common.dto.ApiResponse;
import com.example.weaponMaster.modules.slack.constant.AdminSlackChannelType;
import com.example.weaponMaster.modules.slack.service.SlackService;
import com.example.weaponMaster.modules.slack.util.HtmlUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository     articleRepository;
    private final CommentRepository     commentRepository;
    private final UserInfoRepository    userInfoRepository;
    private final SlackService          slackService;
    private final UserLogService        userLogService;
    private final UserPermissionService userPermissionService;

    @Transactional
    public ApiResponse<Void> createArticle(ReqArticlesDto request) {
        Article article = new Article(
                request.getCategoryType(),
                request.getArticleType(),
                request.getArticleDetailType(),
                request.getTitle(),
                request.getContents(),
                request.getUserId()
        );

        if (userPermissionService.isAdminAuthorized(request.getIsAdminMode(), request.getUserId())) {
            article.setIsAdminMode(true);
        }

        Article userArticle = articleRepository.save(article);
        if(userArticle.getCategoryType() == CategoryType.SERVICE_CENTER) {
            if (userArticle.getArticleType() == ArticleType.SERVICE_CENTER.PRIVATE_CONTACT) {
                slackService.sendMessageAdmin(AdminSlackChannelType.PRIVATE_CONTACT_NOTICE, getNoticeMessage(userArticle));
            }
        }

        userLogService.saveLog(request.getUserId(), request.getIsAdminMode(), LogContentsType.ARTICLE, LogActType.CREATE, (short)(int)userArticle.getId());
        return ApiResponse.success();
    }

    private String getNoticeMessage(Article userArticle) {
        // 1. HTML 태그 제거 및 정리
        String plainText = HtmlUtil.getPlainText(userArticle.getContents());

        // 2. 길이 제한
        int maxLength = 80;
        if (plainText.length() > maxLength) {
            plainText = plainText.substring(0, maxLength) + "...";
        }

        // 이모지코드: 📩
        String link = String.format("%s/service/%d", MyURL.WEAPON_MASTER, userArticle.getId());
        String message = String.format(
                "`\uD83D\uDCE9 1:1 새 문의 등록` - <%s|링크 바로가기>\n" +
                        "```" +
                        "제목: %s\n" +
                        "이름: %s\n" +
                        "본문: %s" +
                        "```",
                link,
                userArticle.getTitle(),
                userArticle.getUserId(),
                plainText
        );
        return message;
    }

    // TODO 수정 권한 있는지 체크 필요
    @Transactional
    public ApiResponse<Void> updateArticle(ReqArticlesDto request, Integer id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Article not found: " + id));

        article.update(request);
        Article savedArticle = articleRepository.save(article);

        userLogService.saveLog(request.getUserId(), request.getIsAdminMode(), LogContentsType.ARTICLE, LogActType.UPDATE, (short)(int)savedArticle.getId());
        return ApiResponse.success();
    }

    // TODO 삭제 권한 있는지 체크 필요
    @Transactional
    public ApiResponse<Void> deleteArticle(ReqArticlesDto request, Integer articleId) {
        Article article = articleRepository.findById(articleId).orElse(null);
        if (article == null) {
           throw new IllegalArgumentException(String.format("[게시물 삭제 에러] Article not found. userId: %s, articleId: %d", request.getUserId(), articleId));
        }

        if (article.getCategoryType() == CategoryType.NEWS) {
            UserInfo userInfo = userInfoRepository.findByUserId(request.getUserId());
            if (userInfo == null || userInfo.getUserType() == UserType.NORMAL) {
                throw new IllegalArgumentException(String.format("[게시물 삭제 에러] Permission denied. userId: %s, articleId: %d", request.getUserId(), articleId));
            }
        }

        // 게시물 삭제
        articleRepository.deleteById(articleId);

        // 게시물 댓글 삭제
        commentRepository.deleteByArticleId(articleId);

        userLogService.saveLog(request.getUserId(), request.getIsAdminMode(), LogContentsType.ARTICLE, LogActType.DELETE, (short)(int)articleId);
        return ApiResponse.success();
    }

    public ArticleDto[] getBestViewArticles(Integer articleCount) {
        Article[]    articles       = articleRepository.findBestViewArticles(articleCount);
        ArticleDto[] articleDtoList = Arrays.stream(articles)
                                        .map(this::convertToDto)
                                        .toArray(ArticleDto[]::new);

        return articleDtoList;
    }

    public ArticleDto getLatestDevNote() {
        Article article = articleRepository.findLatestDevNote(CategoryType.NEWS, ArticleType.NEWS.DEV_NOTE);
        return convertToDto(article);
    }

    public ApiResponse<ArticleDto[]> getArticleList(Integer categoryType, Integer articleType) {
        Article[] articles = (articleType == ArticleType.ALL)
                ? articleRepository.findArticlesByCategory(categoryType)
                : articleRepository.findArticleList(categoryType, articleType);

        return ApiResponse.success(Arrays.stream(articles)
                .map(this::convertToDto)
                .toArray(ArticleDto[]::new));
    }

    public ApiResponse<ArticleDto> getArticle(Integer articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException(String.format("[게시물 읽기 에러] Article not found. articleId: %d", articleId)));

        article.setViewCount(article.getViewCount() + 1);
        Article updatedArticle = articleRepository.save(article);

        return ApiResponse.success(convertToDto(updatedArticle));
    }

    private ArticleDto convertToDto(Article article) {
        return ArticleDto.builder()
                .id(article.getId())
                .categoryType(article.getCategoryType())
                .articleType(article.getArticleType())
                .articleDetailType(article.getArticleDetailType())
                .title(article.getTitle())
                .contents(article.getContents())
                .userId(article.getUserId())
                .createDate(article.getCreateDate())
                .updateDate(article.getUpdateDate())
                .commentCount(article.getCommentCount())
                .viewCount(article.getViewCount())
                .isPinned(article.getIsPinned())
                .isAdminMode((article.getIsAdminMode()))
                .build();
    }
}
