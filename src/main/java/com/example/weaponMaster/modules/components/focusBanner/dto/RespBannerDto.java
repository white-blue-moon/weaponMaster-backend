package com.example.weaponMaster.modules.components.focusBanner.dto;

import java.util.List;
import java.util.Map;

public class RespBannerDto {

    // 배너 타입 번호를 키로 하고, 배너 리스트를 값으로 가지는 Map
    private Map<Integer, List<BannerDto>> banners;

    public Map<Integer, List<BannerDto>> getBanners() {
        return banners;
    }

    public void setBanners(Map<Integer, List<BannerDto>> banners) {
        this.banners = banners;
    }
}
