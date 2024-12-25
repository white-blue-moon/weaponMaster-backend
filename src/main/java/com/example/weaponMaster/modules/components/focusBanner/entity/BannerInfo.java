package com.example.weaponMaster.modules.components.focusBanner.entity;

import jakarta.persistence.*;
import lombok.Getter;

// TODO 이미 DB 상 제약조건이 걸려있으면 코드로는 추가하지 않아도 되는 것으로 보임 -> 다시 확인하기
@Getter
@Entity
@Table(name = "ref_focus_banner_info", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"version", "banner_type", "img_order"})
})
public class BannerInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // TODO 각 필드 @Column(name) 할당하는 게 좋을지 고민
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
}
