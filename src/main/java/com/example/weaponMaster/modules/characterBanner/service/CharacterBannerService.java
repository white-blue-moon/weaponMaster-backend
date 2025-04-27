package com.example.weaponMaster.modules.characterBanner.service;

import com.example.weaponMaster.modules.characterBanner.dto.CharacterBannerDetailDto;
import com.example.weaponMaster.modules.characterBanner.dto.CharacterBannerFullInfoDto;
import com.example.weaponMaster.modules.characterBanner.entity.CharacterBanner;
import com.example.weaponMaster.modules.characterBanner.entity.CharacterBannerDetail;
import com.example.weaponMaster.modules.characterBanner.repository.CharacterBannerDetailRepository;
import com.example.weaponMaster.modules.characterBanner.repository.CharacterBannerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CharacterBannerService {

    private final CharacterBannerRepository        characterBannerRepo;
    private final CharacterBannerDetailRepository  characterBannerDetailRepo;

    public CharacterBannerFullInfoDto[] getBannerFullInfo(Integer version) {
        CharacterBanner[]       banners       = characterBannerRepo.findByVersion(version);       // 배너 기본 정보 가져오기
        CharacterBannerDetail[] bannerDetails = characterBannerDetailRepo.findByVersion(version); // 배너 상세 정보 가져오기 (이미 정렬된 상태)

        CharacterBannerFullInfoDto[] result = new CharacterBannerFullInfoDto[banners.length];
        int findStartIndex = 0;
        int resultIndex    = 0;

        // 캐릭터 타입 별로 필터링해서 full 조합
        for (CharacterBanner banner : banners) {
            List<CharacterBannerDetailDto> filteredDetails = findDetailsByCharacterType(banner.getCharacterType(), bannerDetails, findStartIndex);
            findStartIndex += filteredDetails.size(); // 이미 필터링한 배너 디테일 인덱스 쪽은 건너뜀

            result[resultIndex++] = createFullInfoDto(banner, filteredDetails);
        }

        return result;
    }

    private List<CharacterBannerDetailDto> findDetailsByCharacterType(Byte characterType, CharacterBannerDetail[] bannerDetails, int findStartIndex) {
        List<CharacterBannerDetailDto> filteredDetails = new ArrayList<>();

        for (int i = findStartIndex; i < bannerDetails.length; i++) {
            CharacterBannerDetail bannerDetail = bannerDetails[i];
            if (characterType != bannerDetail.getCharacterType()) {
                break;
            }

            CharacterBannerDetailDto bannerDetailDto = createBannerDetailDto(bannerDetail);
            filteredDetails.add(bannerDetailDto);
        }

        return filteredDetails;
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
