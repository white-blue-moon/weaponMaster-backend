package com.example.weaponMaster.dto;

import java.util.List;

public class BannerResponseDto {

    private List<BannerInfoDto> mainFocusBanners;
    private List<BannerInfoDto> newsBannersFirst;
    private List<BannerInfoDto> newsBannersSecond;

    public BannerResponseDto(List<BannerInfoDto> mainFocusBanners,
                             List<BannerInfoDto> newsBannersFirst,
                             List<BannerInfoDto> newsBannersSecond) {
        this.mainFocusBanners = mainFocusBanners;
        this.newsBannersFirst = newsBannersFirst;
        this.newsBannersSecond = newsBannersSecond;
    }

    public List<BannerInfoDto> getMainFocusBanners() {
        return mainFocusBanners;
    }

    public void setMainFocusBanners(List<BannerInfoDto> mainFocusBanners) {
        this.mainFocusBanners = mainFocusBanners;
    }

    public List<BannerInfoDto> getNewsBannersFirst() {
        return newsBannersFirst;
    }

    public void setNewsBannersFirst(List<BannerInfoDto> newsBannersFirst) {
        this.newsBannersFirst = newsBannersFirst;
    }

    public List<BannerInfoDto> getNewsBannersSecond() {
        return newsBannersSecond;
    }

    public void setNewsBannersSecond(List<BannerInfoDto> newsBannersSecond) {
        this.newsBannersSecond = newsBannersSecond;
    }
}
