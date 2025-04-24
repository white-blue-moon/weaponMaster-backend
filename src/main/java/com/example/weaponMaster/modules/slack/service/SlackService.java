package com.example.weaponMaster.modules.slack.service;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
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

    public ApiResponse<RespSlackDto> getSlackIntegration(String userId, Byte noticeType) {
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
    public ResponseEntity<String> handleSlackOauthCallback(String code, String state) {
        // 이미 등록되어 있는 정보가 있는지 확인
        String          userId        = state;
        UserSlackNotice findUserSlack = userSlackNoticeRepo.findByUserIdAndType(userId, UserSlackNoticeType.AUCTION_NOTICE);
        if (findUserSlack != null) {
            throw new IllegalArgumentException(String.format("[Slack Bot 설치 에러] 이미 등록된 정보가 존재합니다 userId: %s", userId));
        }

        // 1. 슬랙 봇 정보 조회
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
        UserSlackNotice savedUserSlack = userSlackNoticeRepo.save(userSlack);

        // 7. 사용자에게 등록 완료 Slack 알림 전송
        testSlackIntegration(savedUserSlack.getUserId(), savedUserSlack.getNoticeType());

        // 8. 프론트로 보낼 콜백 정보 할당
        String slackInfoJson  = mapper.writeValueAsString(convertToDto(savedUserSlack));
        slackInfoJson         = slackInfoJson.replace("\\", "\\\\").replace("\"", "\\\""); // HTML 내 문자열 이스케이프 (큰 따옴표, 줄바꿈 등)

        String html = """
            <html><body>
            <script>
                if (window.opener) {
                    window.opener.postMessage({
                        success:   true,
                        slackInfo: JSON.parse("%s"),
                    }, "*");
                }
                window.close();
            </script>
            연동이 완료되었습니다. 이 창은 곧 자동으로 닫힐 예정입니다.
            </body></html>
        """.formatted(slackInfoJson);

        return ResponseEntity.ok().contentType(MediaType.TEXT_HTML).body(html);
    }

    @SneakyThrows
    public ApiResponse<Void> testSlackIntegration(String userId, Byte noticeType) {
        // 1. 유저 연동 정보 확인
        UserSlackNotice userSlack = userSlackNoticeRepo.findByUserIdAndType(userId, noticeType);
        if (userSlack == null) {
            throw new IllegalArgumentException(String.format("[Slack 통신 테스트 에러] 슬랙 토큰 정보를 확인할 수 없습니다. userId: %s, noticeType: %d", userId, noticeType));
        }

        // 2. 테스트 메시지 작성
        String message = String.format(
                "`[Slack 연동 확인 알림]`\n" +
                        "```\n" +
                        "%s 님 안녕하세요.\n" +
                        "이 메시지는 Slack 연동 확인을 위해 발송되었습니다. \n" +
                        "이용해 주셔서 감사드립니다. \n" +
                        "```",
                userId
        );

        String payload = String.format("{ \"channel\": \"%s\", \"text\": \"%s\" }", userSlack.getSlackChannelId(), message);
        ResponseEntity<String> response = restClient.post()
                .uri(SlackApi.SEND_MESSAGE_URL)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + userSlack.getSlackBotToken())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(payload)
                .retrieve()
                .toEntity(String.class);

        // 3. 통신 상태 값 확인
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException(String.format("[Slack 에러] slack API 통신에 실패하였습니다. userId: %s, noticeType: %d", userId, noticeType));
        }

        // 4. 메시지 전송 테스트에 실패했을 때 실패 반환
        JsonNode jsonNode = mapper.readTree(response.getBody());
        boolean  isTestOk = jsonNode.path("ok").asBoolean();
        if (!isTestOk) {
            String errorMessage = jsonNode.path("error").asText("테스트 통신 실패"); // error 키 값이 없으면 기본 문구 전달
            return ApiResponse.error(errorMessage);
        }

        return ApiResponse.success();
    }

    public ApiResponse<Void> deleteSlackIntegration(String userId, Byte noticeType) {
        UserSlackNotice userSlack = userSlackNoticeRepo.findByUserIdAndType(userId, noticeType);
        if (userSlack == null) {
            throw new IllegalArgumentException(String.format("[Slack 채널 삭제 에러] 유저의 채널 정보를 확인할 수 없습니다. userId: %s, noticeType: %d", userId, noticeType));
        }

        userSlackNoticeRepo.delete(userSlack);
        return ApiResponse.success();
    }

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

    private UserSlackDto convertToDto(UserSlackNotice userSlack) {
        if (userSlack == null) {
            return null;
        }

        return UserSlackDto.builder()
                .id(userSlack.getId())
                .userId(userSlack.getUserId())
                .noticeType(userSlack.getNoticeType())
                .slackChannelId(userSlack.getSlackChannelId())
                .createDate(userSlack.getCreateDate())
                .build();
    }
}
