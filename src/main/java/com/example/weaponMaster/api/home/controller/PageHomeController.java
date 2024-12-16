package com.example.weaponMaster.api.home.controller;

import com.example.weaponMaster.api.home.dto.ReqHomeDto;
import com.example.weaponMaster.api.home.dto.RespHomeDto;
import com.example.weaponMaster.api.home.service.PageHomeService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PageHomeController {

    private final PageHomeService pageHomeService;

    public PageHomeController(PageHomeService pageHomeService) {
        this.pageHomeService = pageHomeService;
    }

    @PostMapping("/")
    public RespHomeDto getPageHome(@RequestBody ReqHomeDto request) {
        return pageHomeService.getPageHome(request);
    }
}
