package com.example.weaponMaster.api.page.service;

import com.example.weaponMaster.api.page.dto.ReqHomeDto;
import com.example.weaponMaster.api.page.dto.RespHomeDto;
import com.example.weaponMaster.modules.article.constant.ArticleType;
import com.example.weaponMaster.modules.article.constant.CategoryType;
import com.example.weaponMaster.modules.article.dto.ArticleDto;
import com.example.weaponMaster.modules.article.service.ArticleService;
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

    private final SiteSettingService   siteSettingService;
    private final BannerService        bannerService;
    private final ArticleService       articleService;

    public ApiResponse<RespHomeDto> getPageHome(ReqHomeDto request) {
        // 홈페이지 설정 조회
        Settings settings = siteSettingService.getSetting();

        // 홈페이지 설정과 request 값을 조합하여 배너 정보 조회
        Map<Integer, BannerDto[]> bannersMap = bannerService.getBannersMap(request.getBannerTypes(), settings);
        RespHomeDto resp = new RespHomeDto();
        resp.setFocusBanners(bannersMap);

        // 뉴스 배너 게시글 조회
        ArticleDto[] newsArticles = articleService.getArticleList(CategoryType.NEWS, ArticleType.ALL).getData();
        resp.setNewsArticles(newsArticles);

        // 인기 게시물 조회 (리스트 박스 배너)
        final int    ARTICLE_COUNT    = 9; // TODO 조회하는 게시물 개수 (임시 지정)
        ArticleDto[] bestViewArticles = articleService.getBestViewArticles(ARTICLE_COUNT);
        resp.setBestViewArticles(bestViewArticles);

        return ApiResponse.success(resp);
    }
}
