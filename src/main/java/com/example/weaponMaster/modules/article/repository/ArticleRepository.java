package com.example.weaponMaster.modules.article.repository;

import com.example.weaponMaster.modules.article.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Integer> {

    @Query(value = "SELECT * FROM user_article WHERE category_type = :categoryType AND article_type = :articleType ORDER BY id DESC", nativeQuery = true)
    Article[] findArticleList(Integer categoryType, Integer articleType);

    @Query(value = "SELECT * FROM user_article WHERE category_type = :categoryType ORDER BY id DESC", nativeQuery = true)
    Article[] findArticlesByCategory(Integer categoryType);

    @Query(value = "SELECT * FROM user_article ORDER BY view_count DESC LIMIT :articleCount", nativeQuery = true)
    Article[] findBestViewArticles(Integer articleCount);

    @Query(value = "SELECT * FROM user_article WHERE category_type = :categoryType AND article_type = :articleType ORDER BY id DESC LIMIT 1", nativeQuery = true)
    Article findLatestDevNote(Integer categoryType, Integer articleType);
}
