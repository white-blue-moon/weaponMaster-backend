package com.example.weaponMaster.dto;

public class BannerDto {

    private String imgUrl;
    private String imgComment; // TODO 테스트 출력 확인용으로 임시 추가, 추후 삭제 필요

    public BannerDto(String imgUrl, String imgComment) {
        this.imgUrl = imgUrl;
        this.imgComment = imgComment;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getImgComment() {
        return imgComment;
    }

    public void setImgComment(String imgComment) {
        this.imgComment = imgComment;
    }
}

