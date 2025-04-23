package com.example.weaponMaster.modules.slack.service;

import com.example.weaponMaster.api.slack.dto.ReqSlackDto;
import com.example.weaponMaster.api.slack.dto.RespSlackDto;
import com.example.weaponMaster.modules.common.dto.ApiResponse;
import com.example.weaponMaster.modules.slack.constant.SlackApi;
import com.example.weaponMaster.modules.slack.constant.SlackBotType;
import com.example.weaponMaster.modules.slack.constant.UserSlackNoticeType;
import com.example.weaponMaster.modules.slack.dto.UserSlackDto;
import com.example.weaponMaster.modules.slack.entity.SlackBot;
import com.example.weaponMaster.modules.slack.entity.UserSlackNotice;
import com.example.weaponMaster.modules.slack.repository.AdminSlackNoticeRepository;
import com.example.weaponMaster.modules.slack.repository.SlackBotRepository;
import com.example.weaponMaster.modules.slack.repository.UserSlackNoticeRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SlackService {

    private final UserSlackNoticeRepository  userSlackNoticeRepo;
    private final AdminSlackNoticeRepository adminSlackNoticeRepo;
    private final SlackBotRepository         slackBotRepo;
    private final ObjectMapper               mapper;
    private final RestClient                 restClient = RestClient.create();

    public ApiResponse<RespSlackDto> getSlackChannel(String userId, Integer noticeType) {
        // 1. 슬랙봇 정보 확인
        SlackBot slackBot      = slackBotRepo.findByType(SlackBotType.NORMAL_BOT);
        String   botInstallUrl = getBotInstallUrl(slackBot, userId);

        // 2. 유저 슬랙 알림 정보 확인
        UserSlackNotice userSlack = userSlackNoticeRepo.findByUserIdAndType(userId, noticeType);
        UserSlackDto    userDto   = convertToDto(userSlack);

        RespSlackDto resp = new RespSlackDto();
        resp.setSlackBotInstallUrl(botInstallUrl);
        resp.setUserSlackInfo(userDto);

        return ApiResponse.success(resp);
    }

    private String getBotInstallUrl(SlackBot slackBot, String userId) {
        // chat:write -> 봇이 사용자에게 DM 보냄
        // im:history -> 봇이 사용자가 보낸 DM 읽으려면 필요
        // im:write   -> 봇이 DM 채널을 먼저 여는 경우(채널 ID 확인용)
        String scope = "chat:write,im:history,im:write";

        return String.format(
                SlackApi.BOT_INSTALL_URL,
                slackBot.getClientId(),
                scope,
                slackBot.getRedirectUri(),
                userId
        );
    }

    @SneakyThrows
    @Transactional
    public ApiResponse<Void> handleSlackOauthCallback(String code, String state) {
        // 1. 슬랙 봇 정보 조회
        String     userId   = state;
        SlackBot   slackBot = slackBotRepo.findByType(SlackBotType.NORMAL_BOT);

        // 2. request body 세팅
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("code", code);
        formData.add("client_id", slackBot.getClientId());
        formData.add("client_secret", slackBot.getClientSecret());
        formData.add("redirect_uri", slackBot.getRedirectUri());

        // 3. OAuth 토큰 요청
        ResponseEntity<String> response = restClient.post()
                .uri(SlackApi.OAUTH_URL)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .body(formData)
                .retrieve()
                .toEntity(String.class);

        JsonNode jsonNode = mapper.readTree(response.getBody());
        boolean  ok       = jsonNode.path("ok").asBoolean();
        if (!ok) {
            String error = jsonNode.path("error").asText();
            throw new RuntimeException(String.format("[Slack Bot 설치 에러] userId: %s, error: %s", userId, error));
        }

        // 4. 봇 토큰 및 사용자 슬랙 ID 확인
        String accessToken = jsonNode.path("access_token").asText();
        String slackUserId = jsonNode.path("authed_user").path("id").asText();

        // 5. DM 채널 정보 확인
        Map<String, String> dmFormData = new HashMap<>();
        dmFormData.put("users", slackUserId);

        ResponseEntity<String> dmResponse = restClient.post()
                .uri(SlackApi.DM_OPEN_URL)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(dmFormData)
                .retrieve()
                .toEntity(String.class);

        JsonNode dmNode = mapper.readTree(dmResponse.getBody());
        boolean  dmOk   = dmNode.path("ok").asBoolean();
        if (!dmOk) {
            String error = dmNode.path("error").asText();
            throw new RuntimeException(String.format("[Slack DM 채널 오픈 실패] userId: %s, error: %s", userId, error));
        }

        String channelId = dmNode.path("channel").path("id").asText();

        // 6. DB 저장
        UserSlackNotice userSlack = new UserSlackNotice(
                userId,
                UserSlackNoticeType.AUCTION_NOTICE,
                SlackBotType.NORMAL_BOT,
                accessToken,
                channelId
        );
        userSlackNoticeRepo.save(userSlack);

        return ApiResponse.success();
    }


//    @SneakyThrows
//    public ApiResponse<Void> testSlackChannel(ReqSlackDto request) {
//        // 1. 테스트 메시지 작성
//        String message = String.format(
//                "`[Slack 통신 테스트 알림]`\n" +
//                        "```\n" +
//                        "수신자: %s 님 \n" +
//                        "이 메시지는 시스템 알림 테스트를 위해 발송되었습니다. \n" +
//                        "```",
//                request.getUserId()
//        );
//
//        // 2. 슬랙 메시지 전송 테스트 진행
//        SlackBotToken slackToken = slackBotTokenRepo.findByType(Integer.valueOf(SlackBotType.NORMAL_BOT));
//        if (slackToken == null) {
//            throw new IllegalArgumentException(String.format("[Slack 통신 테스트 에러] 슬랙 토큰 정보를 확인할 수 없습니다. userId: %s, noticeType: %d", request.getUserId(), request.getNoticeType()));
//        }
//
//        String payload = String.format("{ \"channel\": \"%s\", \"text\": \"%s\" }", request.getChannelId(), message);
//        ResponseEntity<String> response = restClient.post()
//                .uri(SlackApi.SEND_MESSAGE_URL)
//                .header(HttpHeaders.AUTHORIZATION, "Bearer " + slackToken.getToken())
//                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
//                .body(payload)
//                .retrieve()
//                .toEntity(String.class);
//
//        // 3. 통신 상태 값 확인
//        if (!response.getStatusCode().is2xxSuccessful()) {
//            throw new RuntimeException(String.format("[Slack 에러] slack API 통신에 실패하였습니다. userId: %s, noticeType: %d", request.getUserId(), request.getNoticeType()));
//        }
//
//        // 4. 메시지 전송 테스트에 실패했을 때 실패 반환
//        JsonNode jsonNode = mapper.readTree(response.getBody());
//        boolean  isTestOk = jsonNode.path("ok").asBoolean();
//        if (!isTestOk) {
//            String errorMessage = jsonNode.path("error").asText("테스트 통신 실패"); // error 키 값이 없으면 기본 문구 전달
//            return ApiResponse.error(errorMessage);
//        }
//
//        return ApiResponse.success();
//    }

//    public void sendMessage(String userId, int noticeType, String message) {
//        UserSlackNotice userSlack = userSlackNoticeRepo.findByUserIdAndType(userId, noticeType);
//        // Slack 채널 정보를 등록하지 않은 유저는 Slack 알림 전송은 하지 않음
//        if (userSlack == null) {
//            return;
//        }
//
//        SlackBotToken slackToken = slackBotTokenRepo.findByType(Integer.valueOf(userSlack.getSlackBotType()));
//        if (slackToken == null) {
//            throw new IllegalArgumentException(String.format("[Slack 에러] 슬랙 토큰 정보를 확인할 수 없습니다. userId: %s, noticeType: %d", userId, noticeType));
//        }
//
//        String payload = String.format("{ \"channel\": \"%s\", \"text\": \"%s\" }", userSlack.getSlackChannelId(), message);
//        ResponseEntity<String> response = restClient.post()
//                .uri(SlackApi.SEND_MESSAGE_URL)
//                .header(HttpHeaders.AUTHORIZATION, "Bearer " + slackToken.getToken())
//                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
//                .body(payload)
//                .retrieve()
//                .toEntity(String.class);
//
//        if (!response.getStatusCode().is2xxSuccessful()) {
//            throw new RuntimeException(String.format("[Slack 에러] slack API 통신에 실패하였습니다. userId: %s, noticeType: %d", userId, noticeType));
//        }
//    }

//    public void sendMessageAdmin(int noticeType, String message) {
//        AdminSlackNotice adminSlack = adminSlackNoticeRepo.findByType(noticeType);
//        if (adminSlack == null) {
//            throw new IllegalArgumentException(String.format("[Admin Slack 에러] 관리자 슬랙 알림 정보를 확인할 수 없습니다. noticeType: %d", noticeType));
//        }
//
//        SlackBotToken slackToken = slackBotTokenRepo.findByType(Integer.valueOf(adminSlack.getSlackBotType()));
//        if (slackToken == null) {
//            throw new IllegalArgumentException(String.format("[Admin Slack 에러] 슬랙 토큰 정보를 확인할 수 없습니다. 메시지를 보낼 수 없습니다. noticeType: %d", noticeType));
//        }
//
//        String payload = String.format("{ \"channel\": \"%s\", \"text\": \"%s\" }", adminSlack.getSlackChannelId(), message);
//        ResponseEntity<String> response = restClient.post()
//                .uri(SlackApi.SEND_MESSAGE_URL)
//                .header(HttpHeaders.AUTHORIZATION, "Bearer " + slackToken.getToken())
//                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
//                .body(payload)
//                .retrieve()
//                .toEntity(String.class);
//
//        if (!response.getStatusCode().is2xxSuccessful()) {
//            throw new RuntimeException(String.format("[Admin Slack 에러] slack API 통신에 실패하였습니다 noticeType: %d", noticeType));
//        }
//    }







//    public ApiResponse<Void> registerSlackChannel(ReqSlackDto request) {
//        UserSlackNotice userSlack = userSlackNoticeRepo.findByUserIdAndType(request.getUserId(), Integer.valueOf(request.getNoticeType()));
//        if (userSlack != null) {
//            throw new IllegalArgumentException(String.format("[Slack 채널 등록 에러] 이미 등록된 정보가 존재합니다. userId: %s, noticeType: %d", request.getUserId(), request.getNoticeType()));
//        }
//
//        UserSlackNotice newUserSlack = new UserSlackNotice(
//                request.getUserId(),
//                request.getNoticeType(),
//                SlackBotType.NORMAL_BOT,
//                request.getChannelId()
//        );
//
//        userSlackNoticeRepo.save(newUserSlack);
//        return ApiResponse.success();
//    }

    public ApiResponse<Void> deleteSlackChannel(ReqSlackDto request) {
        UserSlackNotice userSlack = userSlackNoticeRepo.findByUserIdAndType(request.getUserId(), Integer.valueOf(request.getNoticeType()));
        if (userSlack == null) {
            throw new IllegalArgumentException(String.format("[Slack 채널 삭제 에러] 유저의 채널 정보를 확인할 수 없습니다. userId: %s, noticeType: %d", request.getUserId(), request.getNoticeType()));
        }

        userSlackNoticeRepo.delete(userSlack);
        return ApiResponse.success();
    }

    private UserSlackDto convertToDto(UserSlackNotice userSlack) {
        if (userSlack == null) {
            return null;
        }

        return UserSlackDto.builder()
                .id(userSlack.getId())
                .userId(userSlack.getUserId())
                .noticeType(userSlack.getNoticeType())
                .slackChannelId(userSlack.getSlackChannelId())
                .build();
    }
}
