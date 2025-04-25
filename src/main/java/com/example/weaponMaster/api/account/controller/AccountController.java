package com.example.weaponMaster.api.account.controller;

import com.example.weaponMaster.api.account.dto.ReqJoinDto;
import com.example.weaponMaster.api.account.dto.ReqLoginDto;
import com.example.weaponMaster.modules.account.service.UserInfoService;
import com.example.weaponMaster.modules.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AccountController {

    private final UserInfoService userInfoService;

    // 아이디 중복체크
    @GetMapping("/accounts/{userId}/exists")
    public ApiResponse<Boolean> checkUserIdExist(@PathVariable("userId") String userId) {
        boolean isUserIdAvailable = userInfoService.isUserIdAvailable(userId);
        return ApiResponse.success(isUserIdAvailable);
    }

    // 회원가입
    @PostMapping("/accounts")
    public ApiResponse<Void> join(@RequestBody ReqJoinDto request) {
        return userInfoService.createUserInfo(request);
    }

    // 로그인
    @PostMapping("/accounts/login")
    public ApiResponse<Void> login(@RequestBody ReqLoginDto request) {
        return userInfoService.login(request);
    }

    // TODO 토큰 통신으로 개선할 때 로그아웃 API 생성 필요
}
