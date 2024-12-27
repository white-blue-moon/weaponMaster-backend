package com.example.weaponMaster.api.account.login.controller;

import com.example.weaponMaster.api.account.login.dto.ReqLoginDto;
import com.example.weaponMaster.api.account.login.dto.RespLoginDto;
import com.example.weaponMaster.modules.account.service.UserInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class LoginController {

    private final UserInfoService userInfoService;

    // 일반모드 로그인
    @PostMapping("/account/login/normal")
    public RespLoginDto loginNormal(@RequestBody ReqLoginDto request) {
        boolean isLoginOk = userInfoService.canLogin(request.getUserId(), request.getUserPw());
        return new RespLoginDto(isLoginOk);
    }

    // TODO 관리자모드 로그인 API
    // @PostMapping("/account/login/admin")
    // public RespLoginDto loginAdmin(@RequestBody ReqLoginDto request) {
    //     return
    // }
}

