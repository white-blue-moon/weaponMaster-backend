package com.example.weaponMaster.modules.slack.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_slack_notice", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "notice_type"})) // TODO uniqueConstraints 를 선언하면 얻는 장점에 대해 알아보기
public class UserSlackNotice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Integer id;

    @Column(name = "user_id", nullable = false, length = 255)
    private String userId;

    @Column(name = "notice_type", nullable = false)
    private Byte noticeType; // TINYINT 타입 매핑

    @Column(name = "slack_bot_token", nullable = false, length = 255)
    private String slackBotToken;

    @Column(name = "slack_channel_id", nullable = false, length = 255)
    private String slackChannelId;

    @Column(name = "create_date", nullable = false, updatable = false)
    private LocalDateTime createDate;

    @Column(name = "update_date", nullable = false)
    private LocalDateTime updateDate;
}
