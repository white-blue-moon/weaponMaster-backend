package com.example.weaponMaster.modules.siteSetting.dto;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Map;

public class SiteSettingDto {

    private Map<String, Object> settings;
    private String settingsComment;

    // TODO 기본 생성자가 꼭 필요할지 고려해 보기
    // 기본 생성자
    public SiteSettingDto() {
    }

    // 모든 필드를 매개변수로 받는 생성자
    public SiteSettingDto(Map<String, Object> settings, String settingsComment) {
        this.settings = settings;
        this.settingsComment = settingsComment;
    }

    // Getter and Setter
    public Map<String, Object> getSettings() {
        return settings;
    }

    public void setSettings(Map<String, Object> settings) {
        this.settings = settings;
    }

    public String getSettingsComment() {
        return settingsComment;
    }

    public void setSettingsComment(String settingsComment) {
        this.settingsComment = settingsComment;
    }
}
