package com.example.weaponMaster.api.articles.controller;

import com.example.weaponMaster.api.articles.dto.ReqArticlesDto;
import com.example.weaponMaster.api.articles.dto.RespArticlesDto;
import com.example.weaponMaster.modules.components.article.dto.ArticleDto;
import com.example.weaponMaster.modules.components.article.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ArticlesController {

    private final ArticleService articleService;

    // 게시물 등록
    @PostMapping("/articles")
    public RespArticlesDto createArticle(@RequestBody ReqArticlesDto request) {
        boolean isSuccess = articleService.saveArticle(request);
        return new RespArticlesDto(isSuccess);
    }

    // 게시물 수정
    @PutMapping("/articles")
    public RespArticlesDto updateArticle(@RequestBody ReqArticlesDto request) {
        boolean isSuccess = articleService.saveArticle(request);
        return new RespArticlesDto(isSuccess);
    }

    // 게시물 리스트 조회
    @GetMapping("/articles/list/{categoryType}/{articleType}")
    public RespArticlesDto getArticleList(@PathVariable("categoryType") Integer categoryType, @PathVariable("articleType") Integer articleType) {
        ArticleDto[] articles = articleService.getArticleList(categoryType, articleType);
        return new RespArticlesDto(true, articles);
    }

    // 게시물 조회
    @GetMapping("/articles/{id}")
    public RespArticlesDto getArticle(@PathVariable("id") Integer id) {
        ArticleDto[] article = articleService.getArticle(id);
        return new RespArticlesDto(true, article);
    }
}
