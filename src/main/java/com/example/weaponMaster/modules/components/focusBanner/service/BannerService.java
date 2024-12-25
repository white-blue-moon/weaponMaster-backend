package com.example.weaponMaster.modules.components.focusBanner.service;

import com.example.weaponMaster.modules.siteSetting.record.Settings;
import com.example.weaponMaster.modules.components.focusBanner.constant.BannerType;
import com.example.weaponMaster.modules.components.focusBanner.dto.BannerDto;
import com.example.weaponMaster.modules.components.focusBanner.dto.RespBannerDto;
import com.example.weaponMaster.modules.components.focusBanner.dto.ReqBannerDto;
import com.example.weaponMaster.modules.components.focusBanner.entity.BannerInfo;
import com.example.weaponMaster.modules.components.focusBanner.repository.BannerInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BannerService {

    private final BannerInfoRepository bannerInfoRepository;

    public RespBannerDto getBanners(List<ReqBannerDto> requests, Settings settings) {
        Map<Integer, List<BannerDto>> bannersMap = new HashMap<>();

        for (ReqBannerDto request : requests) {
            // 배너 타입에 매치되는 버전 확인
            Integer version = getBannerVersion(request.getBannerType(), settings);

            // 배너 정보를 DB 에서 조회
            List<BannerInfo> banners = bannerInfoRepository
                    .findByVersionAndTypeSorted(version, request.getBannerType());

            // 각 배너 타입에 맞게 리스트를 설정
            List<BannerDto> bannerDtoList = banners.stream()
                    .map(banner -> new BannerDto(banner.getImgUrl(), banner.getImgComment()))
                    .collect(Collectors.toList());

            bannersMap.put(request.getBannerType(), bannerDtoList);
        }

        RespBannerDto responseDto = new RespBannerDto();
        responseDto.setBanners(bannersMap);
        return responseDto;
    }

    private Integer getBannerVersion(Integer bannerType, Settings settings) {
        return switch (bannerType) {
            case BannerType.MAIN_FOCUS_BANNER -> settings.homeMainFocusVer();
            case BannerType.NEWS_BANNER_FIRST -> settings.homeNewsFocusFirstVer();
            case BannerType.NEWS_BANNER_SECOND -> settings.homeNewsFocusSecondVer();
            default -> throw new IllegalArgumentException("Invalid banner type: " + bannerType);
        };
    }

}
