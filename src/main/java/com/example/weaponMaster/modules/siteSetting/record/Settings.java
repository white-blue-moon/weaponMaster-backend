package com.example.weaponMaster.modules.siteSetting.record;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Settings(
        // 퍼블리셔 로고
        @JsonProperty("publisher_logo_ver") int publisherLogoVer,

        // home 화면 포커스 배너
        @JsonProperty("home_main_focus_ver")        int homeMainFocusVer,
        @JsonProperty("home_news_focus_first_ver")  int homeNewsFocusFirstVer,
        @JsonProperty("home_news_focus_second_ver") int homeNewsFocusSecondVer,

        // 점검페이지 포커스 배너
        @JsonProperty("inspection_main_focus_ver")  int inspectionMainFocusVer,

        // 캐릭터 배너
        @JsonProperty("character_banner_ver")       int characterBannerVer
) {}
