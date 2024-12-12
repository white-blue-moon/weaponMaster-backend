package com.example.weaponMaster.api.test.controller;

import com.example.weaponMaster.modules.siteSetting.dto.SiteSettingDto;
import com.example.weaponMaster.modules.siteSetting.service.SiteSettingService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    private final SiteSettingService siteSettingService;

    public TestController(SiteSettingService siteSettingService) {
        this.siteSettingService = siteSettingService;
    }

    @PostMapping("/settings")
    public SiteSettingDto getSiteSetting() {
        return siteSettingService.getSetting();
    }
}
