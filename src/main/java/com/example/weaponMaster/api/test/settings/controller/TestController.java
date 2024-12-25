package com.example.weaponMaster.api.test.settings.controller;

import com.example.weaponMaster.modules.siteSetting.record.Settings;
import com.example.weaponMaster.modules.siteSetting.service.SiteSettingService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    private final SiteSettingService siteSettingService;

    public TestController(SiteSettingService siteSettingService) {
        this.siteSettingService = siteSettingService;
    }

    @PostMapping("/test/settings")
    public Settings getSiteSetting() {
        return siteSettingService.getSetting();
    }
}
