package com.example.weaponMaster.api.articles.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ReqArticlesDto {

    private final Integer id;
    private final Integer categoryType;
    private final Integer articleType;
    private final Integer articleDetailType;
    private final String title;
    private final String contents;
    private final String author;
}
