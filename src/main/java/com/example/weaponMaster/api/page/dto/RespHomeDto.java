package com.example.weaponMaster.api.page.dto;

import com.example.weaponMaster.modules.focusBanner.dto.BannerDto;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@Data
@RequiredArgsConstructor
public class RespHomeDto {

    // Map<키: 배너 타입 번호, 값: 배너 리스트>
    private final Map<Integer, BannerDto[]> focusBanners;

    // TODO 뉴스 리스트
    // TODO 박스 리스트
}
