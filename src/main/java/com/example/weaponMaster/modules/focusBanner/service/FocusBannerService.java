package com.example.weaponMaster.modules.focusBanner.service;

import com.example.weaponMaster.modules.siteSetting.record.Settings;
import com.example.weaponMaster.modules.focusBanner.constant.FocusBannerType;
import com.example.weaponMaster.modules.focusBanner.dto.BannerDto;
import com.example.weaponMaster.modules.focusBanner.entity.BannerInfo;
import com.example.weaponMaster.modules.focusBanner.repository.BannerInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FocusBannerService {

    private final BannerInfoRepository bannerInfoRepository;

    // Map<키: 배너 타입 번호, 값: 배너 리스트> 반환
    public Map<Integer, BannerDto[]> getBannersMap(int[] bannerTypes, Settings settings) {
        Map<Integer, BannerDto[]> bannersMap = new HashMap<>();

        for (int bannerType : bannerTypes) {
            // 배너 타입에 매치되는 버전 확인
            int version = getBannerVersion(bannerType, settings);

            // 배너 정보 DB 조회
            BannerInfo[] banners = bannerInfoRepository.findByVersionAndType(version, bannerType);
            if (banners == null || banners.length == 0) {
                throw new IllegalArgumentException("can't find focus banner info [bannerType]: " + bannerType + ", [version]: " + version);
            }

            // Map 저장
            bannersMap.put(bannerType, Arrays.stream(banners)
                    .map(banner -> new BannerDto(banner.getImgUrl(), banner.getImgComment()))
                    .toArray(BannerDto[]::new));
        }

        return bannersMap;
    }

    private int getBannerVersion(int bannerType, Settings settings) {
        return switch (bannerType) {
            case FocusBannerType.MAIN_FOCUS_BANNER  -> settings.homeMainFocusVer();
            case FocusBannerType.NEWS_BANNER_FIRST  -> settings.homeNewsFocusFirstVer();
            case FocusBannerType.NEWS_BANNER_SECOND -> settings.homeNewsFocusSecondVer();
            case FocusBannerType.MAINTENANCE_MAIN -> settings.inspectionMainFocusVer();
            default -> throw new IllegalArgumentException("Invalid focus banner type: " + bannerType);
        };
    }
}
