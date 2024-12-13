package com.example.weaponMaster.modules.siteSetting.service;

import com.example.weaponMaster.modules.common.records.Settings;
import com.example.weaponMaster.modules.siteSetting.dto.SiteSettingDto;
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
            // JSON (Map<String, Object>) -> Settings 객체로 변환
            Settings convertedValue = objectMapper.convertValue(settingInfo.getSettings(), Settings.class);
            return convertedValue;
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse settings JSON", e);
        }
    }
}
