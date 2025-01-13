package com.example.weaponMaster.api.articles.controller;

import com.example.weaponMaster.api.articles.dto.ReqArticlesDto;
import com.example.weaponMaster.api.articles.dto.RespArticlesDto;
import com.example.weaponMaster.modules.article.dto.ArticleDto;
import com.example.weaponMaster.modules.article.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ArticlesController {

    private final ArticleService articleService;

    // 게시물 등록
    @PostMapping("/articles")
    public RespArticlesDto createArticle(@RequestBody ReqArticlesDto request) {
        boolean isSuccess = articleService.createArticle(request);
        return new RespArticlesDto(isSuccess);
    }

    // 게시물 수정
    @PutMapping("/articles/{id}")
    public RespArticlesDto updateArticle(@RequestBody ReqArticlesDto request, @PathVariable("id") Integer id) {
        boolean isSuccess = articleService.updateArticle(request, id);
        return new RespArticlesDto(isSuccess);
    }

    // 게시물 삭제
    @DeleteMapping("/articles/{id}")
    public RespArticlesDto deleteArticle(@RequestBody ReqArticlesDto request, @PathVariable("id") Integer id) {
        boolean isSuccess = articleService.deleteArticle(request, id);
        return new RespArticlesDto(isSuccess);
    }

    // 게시물 리스트 조회
    @GetMapping("/articles")
    public RespArticlesDto getArticleList(@RequestParam("categoryType") Integer categoryType, @RequestParam("articleType") Integer articleType) {
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
