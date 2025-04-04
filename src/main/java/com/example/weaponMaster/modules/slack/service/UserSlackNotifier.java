package com.example.weaponMaster.modules.slack.service;

import com.example.weaponMaster.modules.slack.constant.SlackApi;
import com.example.weaponMaster.modules.slack.entity.UserSlackNotice;
import com.example.weaponMaster.modules.slack.repository.UserSlackNoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class UserSlackNotifier {

    private final UserSlackNoticeRepository userSlackNoticeRepo;
    private final RestClient                restClient = RestClient.create();

    public void sendMessage(String userId, int noticeType, String message) {
        UserSlackNotice userSlack = userSlackNoticeRepo.findByUserIdAndType(userId, noticeType);
        if (userSlack == null) {
            System.err.println("Slack 토큰이 없습니다. 메시지를 보낼 수 없습니다.");
            return;
        }

        String payload = String.format("{ \"channel\": \"%s\", \"text\": \"%s\" }", userSlack.getSlackChannelId(), message);
        String response = restClient.post()
                .uri(SlackApi.SEND_MESSAGE_URL)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + userSlack.getSlackBotToken())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(payload)
                .retrieve()
                .body(String.class);

        // TODO API 통신 에러 처리 필요
        return;
    }

}
