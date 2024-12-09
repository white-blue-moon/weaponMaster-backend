package com.example.weaponMaster.controller;

import com.example.weaponMaster.dto.BannerInfoDTO;
import com.example.weaponMaster.service.BannerInfoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BannerInfoController {

    private final BannerInfoService bannerInfoService;

    public BannerInfoController(BannerInfoService bannerInfoService) {
        this.bannerInfoService = bannerInfoService;
    }

    @GetMapping("/banners")
    public List<BannerInfoDTO> getBannersByVersion(@RequestParam Integer version) {
        return bannerInfoService.getBannersByVersion(version);
    }
}

