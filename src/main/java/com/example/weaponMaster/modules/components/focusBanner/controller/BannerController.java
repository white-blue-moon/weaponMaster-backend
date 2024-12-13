package com.example.weaponMaster.modules.components.focusBanner.controller;

import com.example.weaponMaster.modules.components.focusBanner.dto.RespBannerDto;
import com.example.weaponMaster.modules.components.focusBanner.dto.ReqBannerDto;
import com.example.weaponMaster.modules.components.focusBanner.service.BannerService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BannerController {

    private final BannerService bannerService;

    public BannerController(BannerService bannerService) {
        this.bannerService = bannerService;
    }

    @PostMapping("/focus-banners")
    public RespBannerDto getBannersByVersion(@RequestBody List<ReqBannerDto> request) {
        return bannerService.getBanners(request);
    }
}
