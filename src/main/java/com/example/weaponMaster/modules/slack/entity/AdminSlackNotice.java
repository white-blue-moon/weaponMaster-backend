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
@Table(name = "admin_slack_notice", uniqueConstraints = @UniqueConstraint(columnNames = {"channel_type", "slack_bot_type"}))
public class AdminSlackNotice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Integer id;

    @Column(name = "channel_comment", length = 255)
    private String channelComment;

    @Column(name = "channel_type", nullable = false)
    private Byte channelType;

    @Column(name = "slack_bot_type", nullable = false)
    private Byte slackBotType;

    @Column(name = "slack_bot_token", nullable = false, length = 255)
    private String slackBotToken;

    @Column(name = "slack_channel_id", nullable = false, length = 255)
    private String slackChannelId;

    @Column(name = "create_date", nullable = false, updatable = false)
    private LocalDateTime createDate;

    @Column(name = "update_date", nullable = false)
    private LocalDateTime updateDate;
}
