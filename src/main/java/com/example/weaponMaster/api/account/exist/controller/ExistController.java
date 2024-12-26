package com.example.weaponMaster.api.account.exist.controller;

import com.example.weaponMaster.api.account.exist.dto.RespExistDto;
import com.example.weaponMaster.modules.account.service.UserInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ExistController {

    private final UserInfoService userInfoService;

    @GetMapping("/account/exist/{userId}")
    public RespExistDto checkUserIdExist(@PathVariable("userId") String userId) {
        boolean isExist = userInfoService.isUserIdExist(userId);
        return new RespExistDto(isExist);
    }
}
