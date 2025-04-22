package com.example.weaponMaster.api.slack.controller;

import com.example.weaponMaster.api.slack.dto.ReqSlackDto;
import com.example.weaponMaster.api.slack.dto.RespSlackDto;
import com.example.weaponMaster.modules.common.dto.ApiResponse;
import com.example.weaponMaster.modules.slack.service.SlackService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class SlackController {

    private final SlackService slackService;

    // 슬랙 채널 정보 조회
    @GetMapping("/slack/channel")
    public ApiResponse<RespSlackDto> getSlackChannel(
            @RequestParam("userId")     String  userId,
            @RequestParam("noticeType") Integer noticeType) {

        return slackService.getSlackChannel(userId, noticeType);
    }

    // 슬랙 채널 통신 테스트
    @PostMapping("/slack/channel/test")
    public ApiResponse<Void> testSlackChannel(@RequestBody ReqSlackDto request) {
        return slackService.testSlackChannel(request);
    }

    // 슬랙 채널 정보 등록
    @PostMapping("/slack/channel")
    public ApiResponse<Void> registerSlackChannel(@RequestBody ReqSlackDto request) {
        return slackService.registerSlackChannel(request);
    }

    // 슬랙 채널 정보 수정
    // @PutMapping("/slack/channel")

    // // 슬랙 채널 정보 삭제
    // @DeleteMapping("/slack/channel")
    // public ApiResponse<Void> removeAuctionNotice(@RequestBody ReqAuctionDto request) {
    //     return neopleApiService.removeAuctionNotice(request);
    // }
}

