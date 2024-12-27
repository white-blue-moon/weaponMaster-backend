package com.example.weaponMaster.api.account.login.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ReqLoginDto {
    private final String userId;
    private final String userPw;
}
