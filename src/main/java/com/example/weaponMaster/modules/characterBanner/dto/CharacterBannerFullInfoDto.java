package com.example.weaponMaster.modules.characterBanner.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class CharacterBannerFullInfoDto {

    private final Byte                       characterType;
    private final String                     nameImgUrl;
    private final String                     thumbImgUrl;
    private final CharacterBannerDetailDto[] bannerDetails;
}
