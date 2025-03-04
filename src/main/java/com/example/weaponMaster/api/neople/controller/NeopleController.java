package com.example.weaponMaster.api.neople.controller;

import com.example.weaponMaster.api.neople.dto.RespAuctionDto;
import com.example.weaponMaster.modules.common.dto.ApiResponse;
import com.example.weaponMaster.modules.neopleAPI.service.NeopleApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class NeopleController {

    private final NeopleApiService neopleApiService;

    // 경매장 등록 아이템 조회
    @GetMapping("/neople/auction/{itemName}")
    public ApiResponse<RespAuctionDto[]> searchAuction(@PathVariable("itemName") String itemName) {
        return neopleApiService.searchAuction(itemName);
    }
}
