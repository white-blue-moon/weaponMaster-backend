package com.example.weaponMaster.api.page.controller;

import com.example.weaponMaster.api.page.dto.ReqAccessGateDto;
import com.example.weaponMaster.api.page.dto.ReqHomeDto;
import com.example.weaponMaster.api.page.dto.RespHomeDto;
import com.example.weaponMaster.api.page.dto.RespInspectionDto;
import com.example.weaponMaster.api.page.service.PageService;
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

    @GetMapping("/page/maintenance")
    public ApiResponse<RespInspectionDto> getPageMaintenance(@RequestParam("bannerType") Integer bannerType) {
        return pageService.getPageMaintenance(bannerType);
    }

    // 인트로 페이지 비밀번호 확인
    @PostMapping("/page/access-gate/verify")
    public ApiResponse<Void> verifyAccessGate(@RequestBody ReqAccessGateDto request) {
        return pageService.verifyAccessGate(request);
    }
}
