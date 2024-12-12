package com.example.weaponMaster.modules.components.focusBanner.service;

import com.example.weaponMaster.modules.components.focusBanner.dto.BannerDto;
import com.example.weaponMaster.modules.components.focusBanner.dto.RespBannerDto;
import com.example.weaponMaster.modules.components.focusBanner.dto.ReqBannerDto;
import com.example.weaponMaster.modules.components.focusBanner.entity.BannerInfo;
import com.example.weaponMaster.modules.components.focusBanner.repository.BannerInfoRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BannerService {

    private final BannerInfoRepository bannerInfoRepository;

    public BannerService(BannerInfoRepository bannerInfoRepository) {
        this.bannerInfoRepository = bannerInfoRepository;
    }

    public RespBannerDto getBanners(List<ReqBannerDto> requests) {
        Map<Integer, List<BannerDto>> bannersMap = new HashMap<>();

        // 배너 타입에 따른 리스트를 생성하고, 각 리스트를 설정
        for (ReqBannerDto request : requests) {
            List<BannerInfo> banners = bannerInfoRepository
                    .findByVersionAndTypeSorted(request.getVersion(), request.getBannerType());

            // 각 배너 타입에 맞게 리스트를 설정
            List<BannerDto> bannerDtoList = banners.stream()
                    .map(banner -> new BannerDto(banner.getImgUrl(), banner.getImgComment()))
                    .collect(Collectors.toList());

            bannersMap.put(request.getBannerType(), bannerDtoList);
        }

        // 응답 객체 생성
        RespBannerDto responseDto = new RespBannerDto();
        responseDto.setBanners(bannersMap);

        return responseDto;
    }
}
