package com.example.weaponMaster.modules.siteSetting.entity;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Type;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Entity
@Table(name = "site_setting")
public class SiteSetting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "active_state", nullable = false)
    private boolean activeState;

    @Type(JsonType.class)
    @Column(name = "settings", nullable = false)
    private Map<String, Object> settings;

    @Column(name = "settings_comment", length = 255)
    private String settingsComment;

    @Column(name = "create_date", nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createDate;
}
