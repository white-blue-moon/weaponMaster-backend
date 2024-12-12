package com.example.weaponMaster.modules.siteSetting.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import com.fasterxml.jackson.databind.JsonNode;
import org.hibernate.annotations.Type;

import java.util.Map;
import java.util.Objects;

@Entity
@Table(name = "site_setting")
public class SiteSetting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    @Type(JsonType.class)
    @Column(name = "settings", nullable = false)
    private Map<String, Object> settings;

    @Column(name = "settings_comment", length = 255)
    private String settingsComment;

    // Getter 및 Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

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

    // TODO equals, hashCode, toString 중 필요없는 코드는 삭제 필요
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SiteSetting that = (SiteSetting) o;
        return isActive == that.isActive &&
                Objects.equals(id, that.id) &&
                Objects.equals(settings, that.settings) &&
                Objects.equals(settingsComment, that.settingsComment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, isActive, settings, settingsComment);
    }

    @Override
    public String toString() {
        return "SiteSetting{" +
                "id=" + id +
                ", isActive=" + isActive +
                ", settings=" + settings +
                ", settingsComment='" + settingsComment + '\'' +
                '}';
    }
}

