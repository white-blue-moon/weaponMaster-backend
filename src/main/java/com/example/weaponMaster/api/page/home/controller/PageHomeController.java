package com.example.weaponMaster.api.page.home.controller;

import com.example.weaponMaster.api.page.home.dto.ReqHomeDto;
import com.example.weaponMaster.api.page.home.dto.RespHomeDto;
import com.example.weaponMaster.api.page.home.service.PageHomeService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PageHomeController {

    private final PageHomeService pageHomeService;

    public PageHomeController(PageHomeService pageHomeService) {
        this.pageHomeService = pageHomeService;
    }

    // TODO /page/home -> 경로는 실제 프론트엔드에서의 일반 / 경로와 동일하므로 경로 수정 고민 필요
    @PostMapping("/page/home")
    public RespHomeDto getPageHome(@RequestBody ReqHomeDto request) {
        return pageHomeService.getPageHome(request);
    }
}
