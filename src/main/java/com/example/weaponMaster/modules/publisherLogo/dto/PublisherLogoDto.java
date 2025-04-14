package com.example.weaponMaster.modules.publisherLogo.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PublisherLogoDto {

    private Integer id;         // 기본 키
    private Integer version;    // 배너 버전
    private String  imgUrl;     // 로고 이미지 소스 링크
    private String  alt;        // 로고 이미지 설명 (alt용)

}