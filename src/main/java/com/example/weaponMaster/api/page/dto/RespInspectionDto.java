package com.example.weaponMaster.api.page.dto;

import com.example.weaponMaster.modules.article.dto.ArticleDto;
import com.example.weaponMaster.modules.focusBanner.dto.BannerDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
public class RespInspectionDto {

    private Map<Integer, BannerDto[]> focusBanners; // Map<키: 배너 타입 번호, 값: 배너 리스트>
    private ArticleDto                devNote;
}


