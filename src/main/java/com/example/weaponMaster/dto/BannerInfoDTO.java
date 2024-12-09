package com.example.weaponMaster.dto;

public class BannerInfoDTO {
    private Integer id;
    private String imgUrl;
    private String imgComment;

    public BannerInfoDTO(Integer id, String imgUrl, String imgComment) {
        this.id = id;
        this.imgUrl = imgUrl;
        this.imgComment = imgComment;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

