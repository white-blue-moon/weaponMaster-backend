package com.example.weaponMaster.api.page.home.controller;

import com.example.weaponMaster.api.page.home.dto.ReqHomeDto;
import com.example.weaponMaster.api.page.home.dto.RespHomeDto;
import com.example.weaponMaster.api.page.home.service.PageHomeService;
import com.example.weaponMaster.modules.components.focusBanner.dto.ReqBannerDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PageHomeController {

    private final PageHomeService pageHomeService;

    public PageHomeController(PageHomeService pageHomeService) {
        this.pageHomeService = pageHomeService;
    }

    @PostMapping("/page/home")
    public RespHomeDto getPageHome(@RequestBody ReqHomeDto request) {
        return pageHomeService.getPageHome(request);
    }
}
