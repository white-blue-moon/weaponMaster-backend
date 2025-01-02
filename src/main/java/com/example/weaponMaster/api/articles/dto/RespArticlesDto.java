package com.example.weaponMaster.api.articles.dto;

import com.example.weaponMaster.modules.components.article.dto.ArticleDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor // save 함수 호출할 때 필요
@AllArgsConstructor
public class RespArticlesDto {
    private boolean isSuccess; // 클라에서 전달받을 때는 is 가 빠진 채로 받음
    private ArticleDto[] articles;

    public RespArticlesDto(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public RespArticlesDto(ArticleDto[] articles) {
        this.articles = articles;
    }
}
