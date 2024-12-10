package com.example.weaponMaster.controller;

import com.example.weaponMaster.dto.RespBannerDto;
import com.example.weaponMaster.dto.ReqBannerDto;
import com.example.weaponMaster.service.BannerService;
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

    @PostMapping("/banners")
    public RespBannerDto getBannersByVersion(@RequestBody List<ReqBannerDto> requests) {
        return bannerService.getBanners(requests);
    }
}
