package com.example.weaponMaster.modules.slack.service;

import com.example.weaponMaster.api.slack.dto.RespSlackDto;
import com.example.weaponMaster.modules.account.constant.LogActType;
import com.example.weaponMaster.modules.account.constant.LogContentsType;
import com.example.weaponMaster.modules.account.service.UserLogService;
import com.example.weaponMaster.modules.common.dto.ApiResponse;
import com.example.weaponMaster.modules.slack.constant.SlackApi;
import com.example.weaponMaster.modules.slack.constant.SlackBotType;
import com.example.weaponMaster.modules.slack.constant.UserSlackNoticeType;
import com.example.weaponMaster.modules.slack.dto.UserSlackDto;
import com.example.weaponMaster.modules.slack.entity.AdminSlackNotice;
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
    private final UserLogService             userLogService;
    private final ObjectMapper               mapper;
    private final RestClient                 restClient = RestClient.create();

    @SneakyThrows
    public void sendMessage(String userId, Byte noticeType, String message) {
        // 1. Slack 연동 정보 확인
        UserSlackNotice userSlack = userSlackNoticeRepo.findByUserIdAndType(userId, noticeType);
        if (userSlack == null) {
            return; // Slack 연동 정보를 등록하지 않은 유저는 Slack 알림 전송 하지 않음
        }

        // 2. payload 준비
        Map<String, Object> payload = new HashMap<>();
        payload.put("channel", userSlack.getSlackChannelId());
        payload.put("text", message);

        // 3. Slack 전송
        ResponseEntity<String> response = restClient.post()
                .uri(SlackApi.SEND_MESSAGE_URL)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + userSlack.getSlackBotToken())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(payload)
                .retrieve()
                .toEntity(String.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException(String.format("[Slack 전송 에러] slack API 통신에 실패하였습니다. userId: %s, noticeType: %d", userId, noticeType));
        }

        JsonNode jsonNode = mapper.readTree(response.getBody());
        boolean  ok       = jsonNode.path("ok").asBoolean();
        if (!ok) {
            String error = jsonNode.path("error").asText();
            throw new RuntimeException(String.format("[Slack 전송 에러] userId: %s, error: %s", userId, error));
        }

        return;
    }

    @SneakyThrows
    public void sendMessageAdmin(int channelType, String message) {
        // 1. Slack 연동 정보 확인
        AdminSlackNotice adminSlack = adminSlackNoticeRepo.findByType(channelType);
        if (adminSlack == null) {
            throw new IllegalArgumentException(String.format("[Admin Slack 전송 에러] 관련 Slack 정보를 DB 에서 확인할 수 없습니다. channelType: %d", channelType));
        }

        // 2. payload 준비
        Map<String, Object> payload = new HashMap<>();
        payload.put("channel", adminSlack.getSlackChannelId());
        payload.put("text", message);

        // 3. Slack 전송
        ResponseEntity<String> response = restClient.post()
                .uri(SlackApi.SEND_MESSAGE_URL)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminSlack.getSlackBotToken())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(payload)
                .retrieve()
                .toEntity(String.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException(String.format("[Admin Slack 전송 에러] slack API 통신에 실패하였습니다. channelType: %d", channelType));
        }

        JsonNode jsonNode = mapper.readTree(response.getBody());
        boolean  ok       = jsonNode.path("ok").asBoolean();
        if (!ok) {
            String error = jsonNode.path("error").asText();
            throw new RuntimeException(String.format("[Admin Slack 전송 에러] channelType: %d, error: %s", channelType, error));
        }

        return;
    }

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
        UserSlackNotice findUserSlack = userSlackNoticeRepo.findByUserIdAndType(userId, UserSlackNoticeType.WEAPON_MASTER_SERVICE_ALERT);
        if (findUserSlack != null) {
            throw new IllegalArgumentException(String.format("[Slack Bot 설치 에러] 이미 등록된 정보가 존재합니다 userId: %s", userId));
        }

        // 1. 슬랙 봇 정보 조회
        SlackBot   slackBot = slackBotRepo.findByType(SlackBotType.NORMAL_BOT);

        // 2. payload 세팅
        MultiValueMap<String, String> payload = new LinkedMultiValueMap<>();
        payload.add("code", code);
        payload.add("client_id", slackBot.getClientId());
        payload.add("client_secret", slackBot.getClientSecret());
        payload.add("redirect_uri", slackBot.getRedirectUri());

        // 3. OAuth 토큰 요청
        ResponseEntity<String> response = restClient.post()
                .uri(SlackApi.OAUTH_URL)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .body(payload)
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
                UserSlackNoticeType.WEAPON_MASTER_SERVICE_ALERT,
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

        userLogService.saveLog(savedUserSlack.getUserId(), false, LogContentsType.SLACK_INTEGRATION, LogActType.CREATE, Short.valueOf(savedUserSlack.getNoticeType()), (short) (int) savedUserSlack.getId());
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

        userLogService.saveLog(userSlack.getUserId(), false, LogContentsType.SLACK_INTEGRATION, LogActType.TEST, Short.valueOf(userSlack.getNoticeType()), (short) (int) userSlack.getId());
        return ApiResponse.success();
    }

    @Transactional
    public ApiResponse<Void> deleteSlackIntegration(String userId, Byte noticeType) {
        UserSlackNotice userSlack = userSlackNoticeRepo.findByUserIdAndType(userId, noticeType);
        if (userSlack == null) {
            throw new IllegalArgumentException(String.format("[Slack 채널 삭제 에러] 유저의 채널 정보를 확인할 수 없습니다. userId: %s, noticeType: %d", userId, noticeType));
        }

        userSlackNoticeRepo.delete(userSlack);
        userLogService.saveLog(userSlack.getUserId(), false, LogContentsType.SLACK_INTEGRATION, LogActType.DELETE, Short.valueOf(userSlack.getNoticeType()), (short) (int) userSlack.getId());
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
                .createDate(userSlack.getCreateDate())
                .build();
    }
}
