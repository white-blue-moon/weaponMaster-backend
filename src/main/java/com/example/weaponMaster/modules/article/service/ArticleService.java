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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final UserInfoRepository userInfoRepository;

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
        articleRepository.save(article);
        return ApiResponse.success();
    }

    @Transactional
    public ApiResponse<Void> updateArticle(ReqArticlesDto request, Integer id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Article not found: " + id));

        article.update(request);
        articleRepository.save(article);
        return ApiResponse.success();
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

        return ApiResponse.success(convertToDto(article));
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
}
