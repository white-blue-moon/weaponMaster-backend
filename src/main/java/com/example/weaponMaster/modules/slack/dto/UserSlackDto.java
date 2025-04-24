package com.example.weaponMaster.modules.slack.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UserSlackDto {

    private Integer       id;                 // DB 고유 row ID
    private String        userId;             // 유저 ID
    private Byte          noticeType;         // 알림 종류
    private Byte          slackBotType;       // 슬랙 봇 종류
    private String        slackChannelId;     // 개인 슬랙 채널 ID
    private LocalDateTime createDate;         // 연동 날짜
}
