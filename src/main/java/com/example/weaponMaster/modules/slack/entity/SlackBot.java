package com.example.weaponMaster.modules.slack.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "slack_bot", uniqueConstraints = @UniqueConstraint(columnNames = {"type"}))
public class SlackBot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Integer id;

    @Column(name = "type_comment", length = 255)
    private String typeComment;

    @Column(name = "type", nullable = false)
    private Byte type;

    @Column(name = "client_id", nullable = false, length = 255)
    private String clientId;

    @Column(name = "client_secret", nullable = false, length = 255)
    private String clientSecret;

    @Column(name = "redirect_uri", nullable = false, length = 255)
    private String redirectUri;

    @Column(name = "create_date", nullable = false, updatable = false)
    private LocalDateTime createDate;

    @Column(name = "update_date", nullable = false)
    private LocalDateTime updateDate;
}