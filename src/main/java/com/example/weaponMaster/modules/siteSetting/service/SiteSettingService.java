package com.example.weaponMaster.modules.siteSetting.service;

import com.example.weaponMaster.modules.siteSetting.record.Settings;
import com.example.weaponMaster.modules.siteSetting.entity.SiteSetting;
import com.example.weaponMaster.modules.siteSetting.repository.SiteSettingRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SiteSettingService {
    private final SiteSettingRepository siteSettingRepository;
    private final ObjectMapper objectMapper; // TODO

    public Settings getSetting() {
        SiteSetting settingInfo = siteSettingRepository.findActiveSetting();
        return objectMapper.convertValue(settingInfo.getSettings(), Settings.class); // JSON 객체 변환
    }
}
