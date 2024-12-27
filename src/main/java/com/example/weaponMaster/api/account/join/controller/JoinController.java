package com.example.weaponMaster.api.account.join.controller;

import com.example.weaponMaster.api.account.join.dto.ReqJoinDto;
import com.example.weaponMaster.api.account.join.dto.RespJoinDto;
import com.example.weaponMaster.modules.account.service.UserInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class JoinController {

    private final UserInfoService userInfoService;

    @PostMapping("/account/join")
    public RespJoinDto join(@RequestBody ReqJoinDto request) {
        boolean isSuccess = userInfoService.createUserInfo(request);
        return new RespJoinDto(isSuccess);
    }
}
