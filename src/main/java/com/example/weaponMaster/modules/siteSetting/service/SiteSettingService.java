package com.example.weaponMaster.modules.siteSetting.service;

import com.example.weaponMaster.modules.common.records.Settings;
import com.example.weaponMaster.modules.siteSetting.entity.SiteSetting;
import com.example.weaponMaster.modules.siteSetting.repository.SiteSettingRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

@Service
public class SiteSettingService {
    private final SiteSettingRepository siteSettingRepository;
    private final ObjectMapper objectMapper;

    public SiteSettingService(SiteSettingRepository siteSettingRepository, ObjectMapper objectMapper) {
        this.siteSettingRepository = siteSettingRepository;
        this.objectMapper = objectMapper;
    }

    public Settings getSetting() {
        SiteSetting settingInfo = siteSettingRepository.findLatestActiveSetting();

        try {
            return objectMapper.convertValue(settingInfo.getSettings(), Settings.class); // JSON 객체 변환
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse settings JSON", e);
        }
    }
}
