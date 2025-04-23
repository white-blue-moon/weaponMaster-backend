package com.example.weaponMaster.api.slack.controller;

import com.example.weaponMaster.api.slack.dto.ReqSlackDto;
import com.example.weaponMaster.api.slack.dto.RespSlackDto;
import com.example.weaponMaster.modules.common.dto.ApiResponse;
import com.example.weaponMaster.modules.slack.service.SlackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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

    // 슬랙봇 설치 및 연동 콜백
    @GetMapping("/slack/oauth/callback")
    public ApiResponse<Void> handleSlackOauthCallback(
            @RequestParam("code")  String code,
            @RequestParam("state") String state) {

        return slackService.handleSlackOauthCallback(code, state);
    }

    // // 슬랙 채널 통신 테스트
    // @PostMapping("/slack/channel/test")
    // public ApiResponse<Void> testSlackChannel(@RequestBody ReqSlackDto request) {
    //     return slackService.testSlackChannel(request);
    // }

     // 슬랙 연동 정보 삭제
     @DeleteMapping("/slack/channel")
     public ApiResponse<Void> deleteSlackChannel(@RequestBody ReqSlackDto request) {
         return slackService.deleteSlackChannel(request);
     }

    // // 슬랙봇 구독 메시지 확인 (Request URL 최초 1회 등록 및 확인용)
    // @PostMapping("/slack/events")
    // public ResponseEntity<Map<String, String>> slackSubscribeEvent(@RequestBody Map<String, Object> payload) {
    //     if ("url_verification".equals(payload.get("type"))) {
    //         String challenge = (String) payload.get("challenge");
    //         return ResponseEntity.ok(Map.of("challenge", challenge));
    //     }

    //     return ResponseEntity.ok(Map.of());
    // }
}

