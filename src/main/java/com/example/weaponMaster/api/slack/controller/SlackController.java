package com.example.weaponMaster.api.slack.controller;

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

    // TODO /slack/events/subscribe 로 경로 수정하기
    // 슬랙봇 Request URL 최초 1회 등록 및 확인용
    @PostMapping("/slack/events")
    public ResponseEntity<Map<String, String>> slackSubscribeEvent(@RequestBody Map<String, Object> payload) {
        String type = (String) payload.get("type");

        // 슬랙이 봇 서버 URL 이 진짜 동작하는지 확인할 때 보내는 요청
        if ("url_verification".equals(type)) {
            String challenge = (String) payload.get("challenge");
            return ResponseEntity.ok(Map.of("challenge", challenge));
        }

        // 그 외 이벤트는 일단 200 OK 로 응답만
        return ResponseEntity.ok(Map.of());
    }

    // 슬랙봇 설치 및 연동 콜백
    @GetMapping("/slack/oauth/callback")
    public ResponseEntity<String> handleSlackOauthCallback(
            @RequestParam("code")  String code,
            @RequestParam("state") String state) {

        return slackService.handleSlackOauthCallback(code, state);
    }

    // 슬랙 응답 확인 통신 테스트
    @PostMapping("/slack/{userId}/integration/test")
    public ApiResponse<Void> testSlackIntegration(
            @PathVariable("userId")     String userId,
            @RequestParam("noticeType") Byte   noticeType) {

        return slackService.testSlackIntegration(userId, noticeType);
    }

    // 슬랙 연동 정보 조회
    @GetMapping("/slack/{userId}/integration")
    public ApiResponse<RespSlackDto> getSlackIntegration(
            @PathVariable("userId")     String userId,
            @RequestParam("noticeType") Byte   noticeType) {

        return slackService.getSlackIntegration(userId, noticeType);
    }

    // 슬랙 연동 정보 삭제
    @DeleteMapping("/slack/{userId}/integration")
    public ApiResponse<Void> deleteSlackIntegration(
            @PathVariable("userId")     String userId,
            @RequestParam("noticeType") Byte   noticeType) {

        return slackService.deleteSlackIntegration(userId, noticeType);
    }
}

