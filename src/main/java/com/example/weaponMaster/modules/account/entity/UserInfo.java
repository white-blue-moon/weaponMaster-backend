package com.example.weaponMaster.modules.account.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_info")
public class UserInfo {

    @Id
    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "user_pw", nullable = false)
    private String userPw;

    @Column(name = "user_type", nullable = false)
    private int userType;

    @Column(name = "df_server_id", nullable = true, columnDefinition = "VARCHAR(255) DEFAULT ''")
    private String dfServerId;

    @Column(name = "df_character_name", nullable = true, columnDefinition = "VARCHAR(255) DEFAULT ''")
    private String dfCharacterName;

    @Column(name = "last_login_date", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime lastLoginDate;

    @Column(name = "join_date", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime joinDate;
}
