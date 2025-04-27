package com.example.weaponMaster.modules.characterBanner.service;

import com.example.weaponMaster.modules.characterBanner.dto.CharacterBannerDetailDto;
import com.example.weaponMaster.modules.characterBanner.dto.CharacterBannerFullInfoDto;
import com.example.weaponMaster.modules.characterBanner.entity.CharacterBanner;
import com.example.weaponMaster.modules.characterBanner.entity.CharacterBannerDetail;
import com.example.weaponMaster.modules.characterBanner.repository.CharacterBannerDetailRepository;
import com.example.weaponMaster.modules.characterBanner.repository.CharacterBannerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CharacterBannerService {

    private final CharacterBannerRepository        characterBannerRepo;
    private final CharacterBannerDetailRepository  characterBannerDetailRepo;

    public CharacterBannerFullInfoDto[] getBannerFullInfo(Integer version) {
        CharacterBanner[]       banners       = characterBannerRepo.findByVersion(version);       // 배너 기본 정보 가져오기
        CharacterBannerDetail[] bannerDetails = characterBannerDetailRepo.findByVersion(version); // 배너 상세 정보 가져오기 (정렬된 상태)

        // characterType 별로 미리 그룹화
        Map<Byte, List<CharacterBannerDetailDto>> detailsMap = groupDetailsByCharacterType(bannerDetails);

        CharacterBannerFullInfoDto[] result = new CharacterBannerFullInfoDto[banners.length];
        int resultIndex = 0;

        // characterType 별로 매칭해서 full info 생성
        for (CharacterBanner banner : banners) {
            List<CharacterBannerDetailDto> bannerDetailList = detailsMap.getOrDefault(banner.getCharacterType(), Collections.emptyList());
            result[resultIndex++] = createFullInfoDto(banner, bannerDetailList);
        }

        return result;
    }

    private Map<Byte, List<CharacterBannerDetailDto>> groupDetailsByCharacterType(CharacterBannerDetail[] bannerDetails) {
        Map<Byte, List<CharacterBannerDetailDto>> map = new HashMap<>();

        for (CharacterBannerDetail detail : bannerDetails) {
            map.computeIfAbsent(detail.getCharacterType(), k -> new ArrayList<>())
                    .add(createBannerDetailDto(detail));
        }

        return map;
    }

    private CharacterBannerDetailDto createBannerDetailDto(CharacterBannerDetail bannerDetail) {
        return new CharacterBannerDetailDto(
                bannerDetail.getCharacterName(),
                bannerDetail.getCharacterIntro(),
                bannerDetail.getImgUrl(),
                bannerDetail.getHomepageLinkUrl(),
                bannerDetail.getCharacterDetailType()
        );
    }

    private CharacterBannerFullInfoDto createFullInfoDto(CharacterBanner banner, List<CharacterBannerDetailDto> currentDetails) {
        return new CharacterBannerFullInfoDto(
                banner.getCharacterType(),
                banner.getNameImgUrl(),
                banner.getThumbImgUrl(),
                currentDetails.toArray(new CharacterBannerDetailDto[0])
        );
    }
}
