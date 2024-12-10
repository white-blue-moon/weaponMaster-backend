package com.example.weaponMaster.service;

import com.example.weaponMaster.constant.BannerType;

import com.example.weaponMaster.dto.BannerInfoDto;
import com.example.weaponMaster.dto.BannerResponseDto;

import com.example.weaponMaster.dto.BannerVersionsDto;
import com.example.weaponMaster.entity.BannerInfo;
import com.example.weaponMaster.repository.BannerInfoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BannerInfoService {

    private final BannerInfoRepository bannerInfoRepository;

    public BannerInfoService(BannerInfoRepository bannerInfoRepository) {
        this.bannerInfoRepository = bannerInfoRepository;
    }

    // TODO 반복문 등으로 코드 간결하게 수정하기
    public BannerResponseDto getBannersByVersion(BannerVersionsDto versions) {
        // 메인 포커스 배너 조회
        List<BannerInfo> mainFocusBanners = bannerInfoRepository
                .findByVersionAndBannerTypeOrderByImgOrder(versions.getMainFocusBannerVer(), BannerType.MAIN_FOCUS_BANNER);

        // 뉴스 배너 첫번째 리스트 조회
        List<BannerInfo> newsBannersFirst = bannerInfoRepository
                .findByVersionAndBannerTypeOrderByImgOrder(versions.getNewsBannerFirstVer(), BannerType.NEWS_BANNER_FIRST);

        // 뉴스 배너 두번째 리스트 조회
        List<BannerInfo> newsBannersSecond = bannerInfoRepository
                .findByVersionAndBannerTypeOrderByImgOrder(versions.getNewsBannerSecondVer(), BannerType.NEWS_BANNER_SECOND);

        // 각 배너 정보를 DTO 로 변환
        List<BannerInfoDto> mainFocusBannersDTO = mainFocusBanners.stream()
                .map(banner -> new BannerInfoDto(banner.getImgUrl(), banner.getImgComment()))
                .collect(Collectors.toList());

        List<BannerInfoDto> newsBannersFirstDTO = newsBannersFirst.stream()
                .map(banner -> new BannerInfoDto(banner.getImgUrl(), banner.getImgComment()))
                .collect(Collectors.toList());

        List<BannerInfoDto> newsBannersSecondDTO = newsBannersSecond.stream()
                .map(banner -> new BannerInfoDto(banner.getImgUrl(), banner.getImgComment()))
                .collect(Collectors.toList());

        // DTO 들을 묶어서 응답 객체 반환
        return new BannerResponseDto(mainFocusBannersDTO, newsBannersFirstDTO, newsBannersSecondDTO);
    }
}
