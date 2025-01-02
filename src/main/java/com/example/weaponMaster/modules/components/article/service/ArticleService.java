package com.example.weaponMaster.modules.components.article.service;

import com.example.weaponMaster.api.news.dto.ReqNewsDto;
import com.example.weaponMaster.modules.components.article.entity.Article;
import com.example.weaponMaster.modules.components.article.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;

    public boolean createArticle(ReqNewsDto request) {
        Article article = new Article(
                request.getCategoryType(),
                request.getArticleType(),
                request.getArticleDetailType(),
                request.getTitle(),
                request.getContents(),
                request.getAuthor()
        );

        // TODO 간결한 에러 처리 방식 확인 중
        try {
            articleRepository.save(article);
            return true;
        } catch (Exception e) {
            System.err.println("Error create article: " + e.getMessage());
            return false;
        }
    }
}
