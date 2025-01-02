package com.example.weaponMaster.modules.components.article.repository;

import com.example.weaponMaster.modules.components.article.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Integer> {

}
