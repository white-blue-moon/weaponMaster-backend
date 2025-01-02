package com.example.weaponMaster.api.news.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ReqNewsDto {

    private final Integer categoryType;
    private final Integer articleType;
    private final Integer articleDetailType;
    private final String title;
    private final String contents;
    private final String author;
}
