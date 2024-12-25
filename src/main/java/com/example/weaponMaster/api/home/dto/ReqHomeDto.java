package com.example.weaponMaster.api.home.dto;

import com.example.weaponMaster.modules.components.focusBanner.dto.ReqBannerDto;
import lombok.Getter;
import java.util.List;

@Getter
public class ReqHomeDto {

    private List<ReqBannerDto> reqBanner;
}
