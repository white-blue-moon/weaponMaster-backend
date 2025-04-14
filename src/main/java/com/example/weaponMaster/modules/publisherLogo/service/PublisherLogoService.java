package com.example.weaponMaster.modules.publisherLogo.service;

import com.example.weaponMaster.modules.common.dto.ApiResponse;
import com.example.weaponMaster.modules.publisherLogo.dto.PublisherLogoDto;
import com.example.weaponMaster.modules.publisherLogo.entity.PublisherLogo;
import com.example.weaponMaster.modules.publisherLogo.repository.PublisherLogoRepository;
import com.example.weaponMaster.modules.siteSetting.record.Settings;
import com.example.weaponMaster.modules.siteSetting.service.SiteSettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PublisherLogoService {

    private final SiteSettingService      siteSettingService;
    private final PublisherLogoRepository publisherIconRepo;

    public ApiResponse<PublisherLogoDto> getPublisherLogo() {
        // 홈페이지 설정 조회
        Settings settings = siteSettingService.getSetting();

        // 설정된 버전의 로고 조회
        PublisherLogo logo = publisherIconRepo.findByVersion(settings.publisherLogoVer());
        return ApiResponse.success(convertToDto(logo));
    }

    private PublisherLogoDto convertToDto(PublisherLogo logo) {
        return PublisherLogoDto.builder()
                .id(logo.getId())
                .version(logo.getVersion())
                .imgUrl(logo.getImgUrl())
                .alt(logo.getAlt())
                .build();
    }
}
