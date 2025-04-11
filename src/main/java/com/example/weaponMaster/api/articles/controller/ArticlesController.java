package com.example.weaponMaster.api.articles.controller;

import com.example.weaponMaster.api.articles.dto.ReqArticlesDto;
import com.example.weaponMaster.modules.article.dto.ArticleDto;
import com.example.weaponMaster.modules.article.service.ArticleService;
import com.example.weaponMaster.modules.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ArticlesController {

    private final ArticleService articleService;

    // 게시물 리스트 조회
    @GetMapping("/articles")
    public ApiResponse<ArticleDto[]> getArticleList(@RequestParam("categoryType") Integer categoryType, @RequestParam("articleType") Integer articleType) {
        return articleService.getArticleList(categoryType, articleType);
    }

    // 게시물 등록
    @PostMapping("/articles")
    public ApiResponse<Void> createArticle(@RequestBody ReqArticlesDto request) {
        return articleService.createArticle(request);
    }

    // 게시물 단일 조회
    @GetMapping("/articles/{id}")
    public ApiResponse<ArticleDto> getArticle(@PathVariable("id") Integer id) {
        return articleService.getArticle(id);
    }

    // 게시물 수정
    @PutMapping("/articles/{id}")
    public ApiResponse<Void> updateArticle(@RequestBody ReqArticlesDto request, @PathVariable("id") Integer id) {
        return articleService.updateArticle(request, id);
    }

    // 게시물 삭제
    @DeleteMapping("/articles/{id}")
    public ApiResponse<Void> deleteArticle(@RequestBody ReqArticlesDto request, @PathVariable("id") Integer id) {
        return articleService.deleteArticle(request, id);
    }
}
