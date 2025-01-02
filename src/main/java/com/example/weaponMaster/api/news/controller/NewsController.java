package com.example.weaponMaster.api.news.controller;

import com.example.weaponMaster.api.news.dto.ReqNewsDto;
import com.example.weaponMaster.api.news.dto.RespNewsDto;
import com.example.weaponMaster.modules.components.article.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class NewsController {

    private final ArticleService articleService;

    // TODO 글을 작성하는 API 경로를 공통화하는 게 좋을지 고민
    @PostMapping("/news/articles")
    public RespNewsDto createArticle(@RequestBody ReqNewsDto request) {
        boolean isSuccess = articleService.createArticle(request);
        return new RespNewsDto(isSuccess);
    }
}
