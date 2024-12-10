package com.example.weaponMaster.controller;

import com.example.weaponMaster.dto.BannerInfoDto;
import com.example.weaponMaster.dto.BannerResponseDto;
import com.example.weaponMaster.dto.BannerVersionsDto;
import com.example.weaponMaster.service.BannerInfoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BannerInfoController {

    private final BannerInfoService bannerInfoService;

    public BannerInfoController(BannerInfoService bannerInfoService) {
        this.bannerInfoService = bannerInfoService;
    }

    @GetMapping("/banners")
    public BannerResponseDto getBannersByVersion(@ModelAttribute BannerVersionsDto versions) {
        return bannerInfoService.getBannersByVersion(versions);
    }
}

