package com.example.weaponMaster.api.page.home.service;

import com.example.weaponMaster.api.page.home.dto.ReqHomeDto;
import com.example.weaponMaster.api.page.home.dto.RespHomeDto;
import com.example.weaponMaster.modules.common.records.Settings;
import com.example.weaponMaster.modules.components.focusBanner.dto.RespBannerDto;
import com.example.weaponMaster.modules.components.focusBanner.service.BannerService;
import com.example.weaponMaster.modules.siteSetting.service.SiteSettingService;
import org.springframework.stereotype.Service;


@Service
public class PageHomeService {

    private final SiteSettingService siteSettingService;
    private final BannerService bannerService;

    public PageHomeService(SiteSettingService siteSettingService, BannerService bannerService) {
        this.siteSettingService = siteSettingService;
        this.bannerService = bannerService;
    }

    public RespHomeDto getPageHome(ReqHomeDto request) {
        // 홈페이지 설정 조회
        Settings settings = siteSettingService.getSetting();
        // 홈페이지 설정과 request 값을 조합하여 배너 정보 조회
        RespBannerDto respBannerDto = bannerService.getBanners(request.getReqBanner(), settings);

        // 응답 객체 생성
        RespHomeDto respHomeDto = new RespHomeDto();
        respHomeDto.setRespBanner(respBannerDto);
        return respHomeDto;
    }
}
