package com.example.weaponMaster.modules.components.focusBanner.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BannerDto {

    private final String imgUrl;
    private final String imgComment; // TODO 테스트 출력 확인용으로 임시 추가, 추후 삭제 필요
}
