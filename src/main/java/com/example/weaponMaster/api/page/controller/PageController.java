package com.example.weaponMaster.api.page.controller;

import com.example.weaponMaster.api.page.dto.ReqHomeDto;
import com.example.weaponMaster.api.page.dto.RespHomeDto;
import com.example.weaponMaster.api.page.dto.RespInspectionDto;
import com.example.weaponMaster.api.page.service.PageService;
import com.example.weaponMaster.modules.article.dto.ArticleDto;
import com.example.weaponMaster.modules.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PageController {

    private final PageService pageService;

    @PostMapping("/page/home")
    public ApiResponse<RespHomeDto> getPageHome(@RequestBody ReqHomeDto request) {
        return pageService.getPageHome(request);
    }

    @GetMapping("/page/inspection/{bannerType}")
    public ApiResponse<RespInspectionDto> getPageInspection(@PathVariable("bannerType") Integer bannerType) {
        return pageService.getPageInspection(bannerType);
    }
}
