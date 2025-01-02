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
        boolean isSuccess = articleService.createArticle(request);
        return new RespArticlesDto(isSuccess);
    }

    // 게시물 리스트 조회
    // TODO 파라미터 int -> Integer 수정 필요할지 확인 필요
    @GetMapping("/articles/list/{categoryType}/{articleType}")
    public RespArticlesDto getArticleList(@PathVariable("categoryType") Integer categoryType, @PathVariable("articleType") Integer articleType) {
        ArticleDto[] articles = articleService.getArticleList(categoryType, articleType);
        return new RespArticlesDto(true, articles);
    }

}
