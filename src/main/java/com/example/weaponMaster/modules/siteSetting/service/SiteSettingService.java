package com.example.weaponMaster.modules.siteSetting.service;

import com.example.weaponMaster.modules.siteSetting.dto.SiteSettingDto;
import com.example.weaponMaster.modules.siteSetting.entity.SiteSetting;
import com.example.weaponMaster.modules.siteSetting.repository.SiteSettingRepository;
import org.springframework.stereotype.Service;

@Service
public class SiteSettingService {
    private final SiteSettingRepository siteSettingRepository;

    public SiteSettingService(SiteSettingRepository siteSettingRepository) {
        this.siteSettingRepository = siteSettingRepository;
    }

    public SiteSettingDto getSetting() {
        SiteSetting settingInfo = siteSettingRepository.findLatestActiveSetting();

        return new SiteSettingDto(
                settingInfo.getSettings(),
                settingInfo.getSettingsComment()
        );
    }
}
