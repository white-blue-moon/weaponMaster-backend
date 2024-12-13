package com.example.weaponMaster.modules.components.focusBanner.dto;

public class ReqBannerDto {

    private Integer bannerType; // 배너 타입 (1: 메인 포커스 배너, 2: 뉴스 배너 첫번째, 3: 뉴스 배너 두번째)
    private Integer version;

    public Integer getBannerType() {
        return bannerType;
    }

    // TODO 타입을 할당하는 경우는 없을 것으로 보여서 제거하는 게 좋을지 검토 필요
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
