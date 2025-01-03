package com.example.weaponMaster.modules.components.article.service;

import com.example.weaponMaster.api.articles.dto.ReqArticlesDto;
import com.example.weaponMaster.modules.components.article.dto.ArticleDto;
import com.example.weaponMaster.modules.components.article.entity.Article;
import com.example.weaponMaster.modules.components.article.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;

    public boolean saveArticle(ReqArticlesDto request) {
        // TODO isPinned 와 같은 값도 반영되도록 수정 필요 (생성, 수정) 공통 사용 중
        Article article = new Article(
                request.getCategoryType(),
                request.getArticleType(),
                request.getArticleDetailType(),
                request.getTitle(),
                request.getContents(),
                request.getAuthor()
        );

        if (request.getId() > 0) {
            article.setId(request.getId());
        }

        try {
            articleRepository.save(article);
            return true;
        } catch (Exception e) {
            System.err.println("Error create article: " + e.getMessage());
            return false;
        }
    }

    public ArticleDto[] getArticleList(Integer categoryType, Integer articleType) {
        Article[] articles;

        try {
            articles = articleRepository.findArticleList(categoryType, articleType);
        } catch (Exception e) {
            System.err.println("Error get article list: " + e.getMessage());
            return null;
        }

        if (articles == null) {
            return null;
        }

        return Arrays.stream(articles)
                .map(article -> ArticleDto.builder()
                        .id(article.getId())
                        .categoryType(article.getCategoryType())
                        .articleType(article.getArticleType())
                        .articleDetailType(article.getArticleDetailType())
                        .title(article.getTitle())
                        .contents(article.getContents())
                        .author(article.getAuthor())
                        .createDate(article.getCreateDate())
                        .updateDate(article.getUpdateDate())
                        .viewCount(article.getViewCount())
                        .isPinned(article.getIsPinned())
                        .build()
                )
                .toArray(ArticleDto[]::new); // 스트림 결과를 ArticleDto[] 배열로 변환
    }

    public ArticleDto[] getArticle(Integer id) {
        Optional<Article> articleOptional;

        try {
            articleOptional = articleRepository.findById(id);
        } catch (Exception e) {
            System.err.println("Error get article by ID: " + e.getMessage());
            return null;
        }

        if (articleOptional.isEmpty()) {
            return null;
        }

        // Optional 에서 값을 가져옴
        Article article = articleOptional.get();

        // Article 객체를 ArticleDto 배열로 변환
        ArticleDto articleDto = ArticleDto.builder()
                .id(article.getId())
                .categoryType(article.getCategoryType())
                .articleType(article.getArticleType())
                .articleDetailType(article.getArticleDetailType())
                .title(article.getTitle())
                .contents(article.getContents())
                .author(article.getAuthor())
                .createDate(article.getCreateDate())
                .updateDate(article.getUpdateDate())
                .viewCount(article.getViewCount())
                .isPinned(article.getIsPinned())
                .build();

        // ArticleDto 를 배열로 반환
        return new ArticleDto[]{articleDto};
    }

}
