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
@Table(name = "admin_slack_notice", uniqueConstraints = @UniqueConstraint(columnNames = {"notice_type"}))
public class AdminSlackNotice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Integer id;

    @Column(name = "notice_type", nullable = false)
    private Byte noticeType; // TINYINT 타입 매핑

    @Column(name = "notice_comment", length = 255)
    private String noticeComment;

    @Column(name = "slack_bot_token", nullable = false, length = 255)
    private String slackBotToken;

    @Column(name = "slack_channel_id", nullable = false, length = 255)
    private String slackChannelId;

    @Column(name = "create_date", nullable = false, updatable = false)
    private LocalDateTime createDate;

    @Column(name = "update_date", nullable = false)
    private LocalDateTime updateDate;
}
