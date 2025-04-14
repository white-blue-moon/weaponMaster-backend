package com.example.weaponMaster.modules.publisherLogo.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "ref_publisher_logo",
        uniqueConstraints = @UniqueConstraint(name = "unique_ref_bannerInfo", columnNames = "version"))
@DynamicUpdate
@Data
@NoArgsConstructor
public class PublisherLogo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "INT COMMENT '기본 키 컬럼'")
    private Integer id;

    @Column(name = "version", nullable = false, columnDefinition = "INT COMMENT '배너 버전'")
    private Integer version;

    @Column(name = "img_url", nullable = false, length = 255, columnDefinition = "VARCHAR(255) COMMENT '로고 이미지 소스 링크'")
    private String imgUrl;

    @Column(name = "alt", nullable = false, length = 255, columnDefinition = "VARCHAR(255) COMMENT '로고 이미지 설명 (alt 기입용)'")
    private String alt;
}
