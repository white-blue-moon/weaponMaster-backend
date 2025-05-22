package com.example.weaponMaster.api.articles.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReqArticlesDto {

    private Boolean isAdminMode;
    private String  userId;
    private Integer categoryType;
    private Integer articleType;
    private Integer articleDetailType;
    private String  title;
    private String  contents;
    private String  adminToken;
}
