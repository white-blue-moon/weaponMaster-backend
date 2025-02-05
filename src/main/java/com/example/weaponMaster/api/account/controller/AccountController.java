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
    @GetMapping("/account/exist/{userId}")
    public ApiResponse<Boolean> checkUserIdExist(@PathVariable("userId") String userId) {
        boolean isUserIdAvailable = userInfoService.isUserIdAvailable(userId);
        return ApiResponse.success(isUserIdAvailable);
    }

    // 회원가입
    @PostMapping("/account/join")
    public ApiResponse<Void> join(@RequestBody ReqJoinDto request) {
        return userInfoService.createUserInfo(request);
    }

    // 일반모드 로그인
    @PostMapping("/account/login/normal")
    public ApiResponse<Void> loginNormal(@RequestBody ReqLoginDto request) {
        return userInfoService.checkLogin(request.getUserId(), request.getUserPw());
    }

    // TODO 관리자모드 로그인 API
}
