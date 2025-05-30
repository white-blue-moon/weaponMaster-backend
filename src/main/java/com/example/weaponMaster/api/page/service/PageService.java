package com.example.weaponMaster.api.page.service;

import com.example.weaponMaster.api.page.dto.ReqAccessGateDto;
import com.example.weaponMaster.api.page.dto.ReqHomeDto;
import com.example.weaponMaster.api.page.dto.RespHomeDto;
import com.example.weaponMaster.api.page.dto.RespInspectionDto;
import com.example.weaponMaster.modules.accessGate.service.AccessGatePasswordService;
import com.example.weaponMaster.modules.account.constant.LogActType;
import com.example.weaponMaster.modules.account.constant.LogContentsType;
import com.example.weaponMaster.modules.account.service.UserLogService;
import com.example.weaponMaster.modules.article.constant.ArticleType;
import com.example.weaponMaster.modules.article.constant.CategoryType;
import com.example.weaponMaster.modules.article.dto.ArticleDto;
import com.example.weaponMaster.modules.article.service.ArticleService;
import com.example.weaponMaster.modules.characterBanner.dto.CharacterBannerFullInfoDto;
import com.example.weaponMaster.modules.characterBanner.service.CharacterBannerService;
import com.example.weaponMaster.modules.common.dto.ApiResponse;
import com.example.weaponMaster.modules.focusBanner.dto.BannerDto;
import com.example.weaponMaster.modules.siteSetting.record.Settings;
import com.example.weaponMaster.modules.focusBanner.service.FocusBannerService;
import com.example.weaponMaster.modules.siteSetting.service.SiteSettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class PageService {

    private final SiteSettingService        siteSettingService;
    private final FocusBannerService        focusBannerService;
    private final ArticleService            articleService;
    private final CharacterBannerService    characterBannerService;
    private final AccessGatePasswordService accessGatePasswordService;
    private final UserLogService            userLogService;

    public ApiResponse<RespHomeDto> getPageHome(ReqHomeDto request) {
        // 홈페이지 설정 조회
        Settings settings = siteSettingService.getSetting();

        // 포커스 배너
        Map<Integer, BannerDto[]> bannersMap = focusBannerService.getBannersMap(request.getBannerTypes(), settings);
        RespHomeDto resp = new RespHomeDto();
        resp.setFocusBanners(bannersMap);

        // 뉴스 배너 게시글
        ArticleDto[] newsArticles = articleService.getArticleList(CategoryType.NEWS, ArticleType.ALL).getData();
        resp.setNewsArticles(newsArticles);

        // 인기 게시물
        final int    ARTICLE_COUNT    = 9; // TODO 조회하는 게시물 개수 (임시 지정)
        ArticleDto[] bestViewArticles = articleService.getBestViewArticles(ARTICLE_COUNT);
        resp.setBestViewArticles(bestViewArticles);

        // 캐릭터 배너
        CharacterBannerFullInfoDto[] characterBanners = characterBannerService.getBannerFullInfo(settings.characterBannerVer());
        resp.setCharacterBanners(characterBanners);

        return ApiResponse.success(resp);
    }

    public ApiResponse<RespInspectionDto> getPageMaintenance(Integer bannerType) {
        // 홈페이지 설정 조회
        Settings settings = siteSettingService.getSetting();

        // 홈페이지 설정과 request 값을 조합하여 배너 정보 조회
        Map<Integer, BannerDto[]> bannersMap = focusBannerService.getBannersMap(new int[]{bannerType}, settings);
        RespInspectionDto resp = new RespInspectionDto();
        resp.setFocusBanners(bannersMap);

        // 최신 개발자 노트 조회
        ArticleDto devNote = articleService.getLatestDevNote();
        resp.setDevNote(devNote);

        return ApiResponse.success(resp);
    }

    public ApiResponse<Void> verifyAccessGate(ReqAccessGateDto request) {
        if (accessGatePasswordService.isPasswordValid(request.getPassword())) {
            // 페이지 접근 해제가 몇 번 정도 이루어지는지 확인을 위해 임시 로그 추가 -> 추후 삭제해도 무방
            userLogService.saveLog("시스템", false, LogContentsType.WEAPON_MASTER, LogActType.LOGIN_ACCESS_GATE);
            return ApiResponse.success();
        }

        return ApiResponse.error("올바르지 않은 비밀코드입니다.");
    }
}
