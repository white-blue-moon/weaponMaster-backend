package com.example.weaponMaster.modules.account.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_log")
@Data
@NoArgsConstructor
public class UserLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Integer id;

    @Column(name = "user_id", nullable = false, length = 255)
    private String userId;

    @Column(name = "is_admin_mode", nullable = false, columnDefinition = "TINYINT DEFAULT 0")
    private Boolean isAdminMode;

    @Column(name = "contents_type", nullable = false)
    private Short contentsType;

    @Column(name = "act_type", nullable = false)
    private Short actType;

    @Column(name = "ref_value", nullable = false, columnDefinition = "INT DEFAULT 0")
    private Short refValue;

    @Column(name = "extra_ref_value", nullable = false, columnDefinition = "INT DEFAULT 0")
    private Short extraRefValue;

    @Column(name = "create_date", nullable = false, insertable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createDate;

    @Column(name = "update_date", nullable = false, insertable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime updateDate;

    public UserLog(String userId, Short contentsType, Short actType) {
        this.userId       = userId;
        this.contentsType = contentsType;
        this.actType      = actType;
    }

    public UserLog(String userId, Boolean isAdminMode, Short contentsType, Short actType) {
        this.userId       = userId;
        this.isAdminMode  = isAdminMode;
        this.contentsType = contentsType;
        this.actType      = actType;
    }

    public UserLog(String userId, Boolean isAdminMode, Short contentsType, Short actType, Short refValue) {
        this.userId       = userId;
        this.isAdminMode  = isAdminMode;
        this.contentsType = contentsType;
        this.actType      = actType;
        this.refValue     = refValue;
    }

    public UserLog(String userId, Boolean isAdminMode, Short contentsType, Short actType, Short refValue, Short extraRefValue) {
        this.userId        = userId;
        this.isAdminMode   = isAdminMode;
        this.contentsType  = contentsType;
        this.actType       = actType;
        this.refValue      = refValue;
        this.extraRefValue = extraRefValue;
    }
}
