package com.example.weaponMaster.api.neople.controller;

import com.example.weaponMaster.api.neople.dto.ReqAuctionDto;
import com.example.weaponMaster.api.neople.dto.RespAuctionDto;
import com.example.weaponMaster.api.neople.dto.RespUserAuctionDto;
import com.example.weaponMaster.modules.common.dto.ApiResponse;
import com.example.weaponMaster.modules.neopleAPI.service.NeopleApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class NeopleController {

    private final NeopleApiService neopleApiService;

    // 경매장 등록 아이템 검색
    @GetMapping("/neople/auction/{itemName}")
    public ApiResponse<RespAuctionDto[]> searchAuction(@PathVariable("itemName") String itemName) throws Exception {
        return neopleApiService.searchAuction(itemName);
    }

    // 경매장 아이템 판매 알림 등록
    @PostMapping("/neople/auction/notice")
    public ApiResponse<Void> registerAuctionNotice(@RequestBody ReqAuctionDto request) {
        return neopleApiService.registerAuctionNotice(request);
    }

    // 경매장 아이템 판매 알림 조회
    @GetMapping("/neople/auction/notice")
    public ApiResponse<RespUserAuctionDto> getAuctionNotice(@RequestParam("userId") String userId) {
        return neopleApiService.getAuctionNotice(userId);
    }

    // 경매장 아이템 판매 알림 해제
    @DeleteMapping("/neople/auction/notice")
    public ApiResponse<Void> removeAuctionNotice(@RequestBody ReqAuctionDto request) {
        return neopleApiService.removeAuctionNotice(request);
    }
}
