package com.example.weaponMaster.dto;

public class ReqBannerDto {

    private Integer bannerType; // 배너 타입 (1: 메인 포커스 배너, 2: 뉴스 배너 첫번째, 3: 뉴스 배너 두번째)
    private Integer version;

    public Integer getBannerType() {
        return bannerType;
    }

    public void setBannerType(Integer bannerType) {
        this.bannerType = bannerType;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
