package com.example.weaponMaster.service;

import com.example.weaponMaster.dto.BannerInfoDTO;
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

    public List<BannerInfoDTO> getBannersByVersion(Integer version) {
        List<BannerInfo> banners = bannerInfoRepository.findByVersion(version);
        return banners.stream()
                .map(banner -> new BannerInfoDTO(banner.getId(), banner.getImgUrl(), banner.getImgComment()))
                .collect(Collectors.toList());
    }
}
