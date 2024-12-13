package com.example.weaponMaster.api.page.home.service;

import com.example.weaponMaster.api.page.home.dto.ReqHomeDto;
import com.example.weaponMaster.api.page.home.dto.RespHomeDto;
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
        return null; // TODO
    }
}
