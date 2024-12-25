package com.example.weaponMaster.api.home.controller;

import com.example.weaponMaster.api.home.dto.ReqHomeDto;
import com.example.weaponMaster.api.home.dto.RespHomeDto;
import com.example.weaponMaster.api.home.service.PageHomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PageHomeController {

    private final PageHomeService pageHomeService;

    @PostMapping("/")
    public RespHomeDto getPageHome(@RequestBody ReqHomeDto request) {
        return pageHomeService.getPageHome(request);
    }
}
