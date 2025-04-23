package com.example.weaponMaster.modules.article.service;

import com.example.weaponMaster.api.articles.dto.ReqArticlesDto;
import com.example.weaponMaster.modules.account.constant.UserType;
import com.example.weaponMaster.modules.account.entity.UserInfo;
import com.example.weaponMaster.modules.account.repository.UserInfoRepository;
import com.example.weaponMaster.modules.article.constant.ArticleType;
import com.example.weaponMaster.modules.article.constant.CategoryType;
import com.example.weaponMaster.modules.article.dto.ArticleDto;
import com.example.weaponMaster.modules.article.entity.Article;
import com.example.weaponMaster.modules.article.repository.ArticleRepository;
import com.example.weaponMaster.modules.common.dto.ApiResponse;
import com.example.weaponMaster.modules.slack.constant.AdminSlackChannelType;
import com.example.weaponMaster.modules.slack.service.SlackService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository  articleRepository;
    private final UserInfoRepository userInfoRepository;
    private final SlackService slackService;

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

        Article userArticle = articleRepository.save(article);
        if(userArticle.getCategoryType() == CategoryType.SERVICE_CENTER) {
            if (userArticle.getArticleType() == ArticleType.SERVICE_CENTER.PRIVATE_CONTACT) {
                // slackService.sendMessageAdmin(AdminSlackChannelType.PRIVATE_CONTACT_NOTICE, getNoticeMessage(userArticle));
            }
        }

        return ApiResponse.success();
    }

    private static String getNoticeMessage(Article userArticle) {
        String link = String.format("http://localhost:8080/service/%d", userArticle.getId());
        String message = String.format(
                "`[1대1 문의 등록 알림]` - <%s|링크 바로가기>\n" +
                        "```" +
                        "제목: %s\n" +
                        "작성자: %s" +
                        "```",
                link,
                userArticle.getTitle(),
                userArticle.getUserId()
        );
        return message;
    }

    @Transactional
    public ApiResponse<Void> updateArticle(ReqArticlesDto request, Integer id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Article not found: " + id));

        article.update(request);
        articleRepository.save(article);
        return ApiResponse.success();
    }

    // TODO 업데이트 방식 하나로 통일 가능할지 고려해 보기
    @Transactional
    public ArticleDto updateArticleDto(ArticleDto articleDto) {
        Article article        = convertToEntity(articleDto);
        Article updatedArticle = articleRepository.save(article);
        return convertToDto(updatedArticle);
    }

    @Transactional
    public ApiResponse<Void> updateCommentCount(Integer articleId, Integer commentCount) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("Article not found: " + articleId));

        article.setCommentCount(commentCount);
        articleRepository.save(article);
        return ApiResponse.success();
    }

    @Transactional
    public ApiResponse<Void> deleteArticle(ReqArticlesDto request, Integer id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Article not found: " + id));

        if (article.getCategoryType() == CategoryType.NEWS) {
            UserInfo userInfo = userInfoRepository.findByUserId(request.getUserId());
            if (userInfo == null || userInfo.getUserType() == UserType.NORMAL) {
                throw new IllegalArgumentException("Permission denied");
            }
        }
        articleRepository.deleteById(id);
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

    public ApiResponse<ArticleDto> getArticle(Integer id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Article not found: " + id));

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
                .build();
    }

    private Article convertToEntity(ArticleDto dto) {
        Article article = new Article(
                dto.getCategoryType(),
                dto.getArticleType(),
                dto.getArticleDetailType(),
                dto.getTitle(),
                dto.getContents(),
                dto.getUserId()
        );

        article.setId(dto.getId());
        article.setCommentCount(dto.getCommentCount());
        article.setViewCount(dto.getViewCount());
        article.setIsPinned(dto.getIsPinned());
        return article;
    }

}
