package com.example.weaponMaster.modules.characterBanner.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class CharacterBannerDetailDto {

    private final String  characterName;
    private final String  characterIntro;
    private final String  imgUrl;
    private final String  homepageLinkUrl;
    private final Byte    characterDetailType;
}
