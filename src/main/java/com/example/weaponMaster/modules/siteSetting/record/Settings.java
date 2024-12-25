package com.example.weaponMaster.modules.siteSetting.record;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Settings(
        @JsonProperty("home_main_focus_ver") int homeMainFocusVer,
        @JsonProperty("home_news_focus_first_ver") int homeNewsFocusFirstVer,
        @JsonProperty("home_news_focus_second_ver") int homeNewsFocusSecondVer
) {}
