package com.example.weaponMaster.modules.components.focusBanner.dto;

import lombok.Getter;

@Getter
public class ReqBannerDto {

    private Integer bannerType; // 배너 타입 (1: 메인 포커스 배너, 2: 뉴스 배너 첫번째, 3: 뉴스 배너 두번째)
}
