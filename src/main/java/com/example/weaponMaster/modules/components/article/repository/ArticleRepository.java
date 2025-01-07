package com.example.weaponMaster.modules.components.article.repository;

import com.example.weaponMaster.modules.account.entity.UserInfo;
import com.example.weaponMaster.modules.components.article.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Integer> {

    @Query(value = "SELECT * FROM ref_article WHERE category_type = :categoryType AND article_type = :articleType ORDER BY id DESC", nativeQuery = true)
    Article[] findArticleList(
            @Param("categoryType") Integer categoryType,
            @Param("articleType") Integer articleType
    );

    @Query(value = "SELECT * FROM ref_article WHERE category_type = :categoryType ORDER BY id DESC", nativeQuery = true)
    Article[] findArticlesByCategory(
            @Param("categoryType") Integer categoryType
    );
}
