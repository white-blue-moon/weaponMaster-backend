package com.example.weaponMaster.api.page.service;

import com.example.weaponMaster.api.page.dto.ReqHomeDto;
import com.example.weaponMaster.api.page.dto.RespHomeDto;
import com.example.weaponMaster.modules.common.dto.ApiResponse;
import com.example.weaponMaster.modules.focusBanner.dto.BannerDto;
import com.example.weaponMaster.modules.siteSetting.record.Settings;
import com.example.weaponMaster.modules.focusBanner.service.BannerService;
import com.example.weaponMaster.modules.siteSetting.service.SiteSettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class PageService {

    private final SiteSettingService siteSettingService;
    private final BannerService bannerService;

    public ApiResponse<RespHomeDto> getPageHome(ReqHomeDto request) {
        // 홈페이지 설정 조회
        Settings settings = siteSettingService.getSetting();

        // 홈페이지 설정과 request 값을 조합하여 배너 정보 조회
        Map<Integer, BannerDto[]> bannersMap = bannerService.getBannersMap(request.getBannerTypes(), settings);

        return ApiResponse.success(new RespHomeDto(bannersMap));
    }
}
