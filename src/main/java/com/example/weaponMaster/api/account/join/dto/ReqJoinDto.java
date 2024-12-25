package com.example.weaponMaster.api.account.join.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ReqJoinDto {

    private final String userId;
    private final String userPw;
    private final String dfServerId;
    private final String dfCharacterName;
}
