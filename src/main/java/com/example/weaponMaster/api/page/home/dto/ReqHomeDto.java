package com.example.weaponMaster.api.page.home.dto;

import com.example.weaponMaster.modules.components.focusBanner.dto.ReqBannerDto;

import java.util.List;

public class ReqHomeDto {

    private List<ReqBannerDto> reqBanner;

    public List<ReqBannerDto> getReqBanner() {
        return reqBanner;
    }

    public void setReqBanner(List<ReqBannerDto> reqBanner) {
        this.reqBanner = reqBanner;
    }
}
