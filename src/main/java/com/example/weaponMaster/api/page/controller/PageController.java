package com.example.weaponMaster.api.page.controller;

import com.example.weaponMaster.api.page.dto.ReqHomeDto;
import com.example.weaponMaster.api.page.dto.RespHomeDto;
import com.example.weaponMaster.api.page.service.PageService;
import com.example.weaponMaster.modules.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PageController {

    private final PageService pageService;

    @PostMapping("/page/home")
    public ApiResponse<RespHomeDto> getPageHome(@RequestBody ReqHomeDto request) {
        return pageService.getPageHome(request);
    }

//    @PostMapping("/page/inspection")
//    public RespHomeDto getPageInspection(@RequestBody ReqHomeDto request) {
//        return pageService.getPageHome(request);
//    }
}
