package com.example.weaponMaster.api.account.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ReqJoinDto {

    private final String userId;
    private final String userPw;
}
