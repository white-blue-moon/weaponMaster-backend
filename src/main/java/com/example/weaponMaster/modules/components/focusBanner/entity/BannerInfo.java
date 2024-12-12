package com.example.weaponMaster.modules.components.focusBanner.entity;

import jakarta.persistence.*;

// TODO 이미 DB 상 제약조건이 걸려있으면 코드로는 추가하지 않아도 되는 것으로 보임 -> 다시 확인하기
@Entity
@Table(name = "ref_focus_banner_info", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"version", "banner_type", "img_order"})
})
public class BannerInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Integer version;

    @Column(nullable = false)
    private Byte bannerType;

    @Column(nullable = false, length = 255)
    private String imgUrl;

    @Column(nullable = false)
    private Short imgOrder;

    @Column(length = 255)
    private String imgComment;

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Byte getBannerType() {
        return bannerType;
    }

    public void setBannerType(Byte bannerType) {
        this.bannerType = bannerType;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Short getImgOrder() {
        return imgOrder;
    }

    public void setImgOrder(Short imgOrder) {
        this.imgOrder = imgOrder;
    }

    public String getImgComment() {
        return imgComment;
    }

    public void setImgComment(String imgComment) {
        this.imgComment = imgComment;
    }
}
