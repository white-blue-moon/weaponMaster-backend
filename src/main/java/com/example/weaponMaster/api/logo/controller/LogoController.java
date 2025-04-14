package com.example.weaponMaster.api.logo.controller;

import com.example.weaponMaster.modules.common.dto.ApiResponse;
import com.example.weaponMaster.modules.publisherLogo.dto.PublisherLogoDto;
import com.example.weaponMaster.modules.publisherLogo.service.PublisherLogoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LogoController {

    private final PublisherLogoService publisherLogoService;

    @GetMapping("/logo/publisher")
    public ApiResponse<PublisherLogoDto> getPublisherLogo() {
        return publisherLogoService.getPublisherLogo();
    }
}