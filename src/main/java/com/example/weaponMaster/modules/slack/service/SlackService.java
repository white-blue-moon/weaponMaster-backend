package com.example.weaponMaster.modules.slack.service;

import com.example.weaponMaster.api.slack.dto.ReqSlackDto;
import com.example.weaponMaster.api.slack.dto.RespSlackDto;
import com.example.weaponMaster.modules.article.dto.ArticleDto;
import com.example.weaponMaster.modules.article.entity.Article;
import com.example.weaponMaster.modules.common.dto.ApiResponse;
import com.example.weaponMaster.modules.slack.constant.SlackApi;
import com.example.weaponMaster.modules.slack.constant.SlackBotType;
import com.example.weaponMaster.modules.slack.constant.UserSlackType;
import com.example.weaponMaster.modules.slack.dto.UserSlackDto;
import com.example.weaponMaster.modules.slack.entity.AdminSlackNotice;
import com.example.weaponMaster.modules.slack.entity.SlackBotToken;
import com.example.weaponMaster.modules.slack.entity.UserSlackNotice;
import com.example.weaponMaster.modules.slack.repository.AdminSlackNoticeRepository;
import com.example.weaponMaster.modules.slack.repository.SlackBotTokenRepository;
import com.example.weaponMaster.modules.slack.repository.UserSlackNoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class SlackService {

    private final UserSlackNoticeRepository  userSlackNoticeRepo;
    private final AdminSlackNoticeRepository adminSlackNoticeRepo;
    private final SlackBotTokenRepository    slackBotTokenRepo;
    private final RestClient                 restClient = RestClient.create();

    // TODO 슬랙 정보를 등록하지 않은 유저도 기능 사용은 가능하도록 에러 처리 개선 필요
    public void sendMessage(String userId, int noticeType, String message) {
        UserSlackNotice userSlack = userSlackNoticeRepo.findByUserIdAndType(userId, noticeType);
        if (userSlack == null) {
            throw new IllegalArgumentException(String.format("[Slack 에러] 유저의 슬랙 알림 정보를 확인할 수 없습니다. userId: %s, noticeType: %d", userId, noticeType));
        }

        SlackBotToken slackToken = slackBotTokenRepo.findByType(Integer.valueOf(userSlack.getSlackBotType()));
        if (slackToken == null) {
            throw new IllegalArgumentException(String.format("[Slack 에러] 슬랙 토큰 정보를 확인할 수 없습니다. userId: %s, noticeType: %d", userId, noticeType));
        }

        String payload = String.format("{ \"channel\": \"%s\", \"text\": \"%s\" }", userSlack.getSlackChannelId(), message);
        ResponseEntity<String> response = restClient.post()
                .uri(SlackApi.SEND_MESSAGE_URL)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + slackToken.getToken())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(payload)
                .retrieve()
                .toEntity(String.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException(String.format("[Slack 에러] slack API 통신에 실패하였습니다. userId: %s, noticeType: %d", userId, noticeType));
        }
    }

    public void sendMessageAdmin(int noticeType, String message) {
        AdminSlackNotice adminSlack = adminSlackNoticeRepo.findByType(noticeType);
        if (adminSlack == null) {
            throw new IllegalArgumentException(String.format("[Admin Slack 에러] 관리자 슬랙 알림 정보를 확인할 수 없습니다. noticeType: %d", noticeType));
        }

        SlackBotToken slackToken = slackBotTokenRepo.findByType(Integer.valueOf(adminSlack.getSlackBotType()));
        if (slackToken == null) {
            throw new IllegalArgumentException(String.format("[Admin Slack 에러] 슬랙 토큰 정보를 확인할 수 없습니다. 메시지를 보낼 수 없습니다. noticeType: %d", noticeType));
        }

        String payload = String.format("{ \"channel\": \"%s\", \"text\": \"%s\" }", adminSlack.getSlackChannelId(), message);
        ResponseEntity<String> response = restClient.post()
                .uri(SlackApi.SEND_MESSAGE_URL)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + slackToken.getToken())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(payload)
                .retrieve()
                .toEntity(String.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException(String.format("[Admin Slack 에러] slack API 통신에 실패하였습니다 noticeType: %d", noticeType));
        }
    }

    public ApiResponse<Void> registerSlackChannel(ReqSlackDto request) {
        UserSlackNotice userSlack = userSlackNoticeRepo.findByUserIdAndType(request.getUserId(), Integer.valueOf(request.getNoticeType()));
        if (userSlack != null) {
            throw new IllegalArgumentException(String.format("[Slack 채널 등록 에러] 이미 등록된 정보가 존재합니다. userId: %s, noticeType: %d", request.getUserId(), request.getNoticeType()));
        }

        UserSlackNotice newUserSlack = new UserSlackNotice(
                request.getUserId(),
                request.getNoticeType(),
                SlackBotType.NORMAL_BOT,
                request.getChannelId()
        );

        userSlackNoticeRepo.save(newUserSlack);
        return ApiResponse.success();
    }

    public ApiResponse<RespSlackDto> getSlackChannel(String userId, Integer noticeType) {
        UserSlackNotice userSlack = userSlackNoticeRepo.findByUserIdAndType(userId, noticeType);

        UserSlackDto    userDto   = convertToDto(userSlack);
        RespSlackDto    resp      = new RespSlackDto();
        resp.setUserSlackInfo(userDto);

        return ApiResponse.success(resp);
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

    private UserSlackNotice convertToEntity(UserSlackDto dto) {
        UserSlackNotice userSlack = new UserSlackNotice(
                dto.getUserId(),
                dto.getNoticeType(),
                dto.getSlackBotType(),
                dto.getSlackChannelId()
        );

        userSlack.setId(dto.getId());
        return userSlack;
    }
}
