package com.example.weaponMaster.modules.account.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDto {

    private boolean isExist;
    private String userId;
    private String userPw;
    private int userType;
    private String dfServerId;
    private String dfCharacterName;
    private LocalDateTime lastLoginDate;
    private LocalDateTime joinDate;
}
