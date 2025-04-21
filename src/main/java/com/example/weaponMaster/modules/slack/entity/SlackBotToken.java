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
@Table(name = "slack_bot_token", uniqueConstraints = @UniqueConstraint(columnNames = {"type"}))
public class SlackBotToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Integer id;

    @Column(name = "type_comment", nullable = false, length = 255)
    private String typeComment;

    @Column(name = "type", nullable = false)
    private Byte type;

    @Column(name = "token", nullable = false, length = 255)
    private String token;

    @Column(name = "create_date", nullable = false, updatable = false)
    private LocalDateTime createDate;

    @Column(name = "update_date", nullable = false)
    private LocalDateTime updateDate;
}