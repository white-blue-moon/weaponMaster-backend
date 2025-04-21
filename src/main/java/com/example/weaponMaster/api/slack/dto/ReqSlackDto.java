package com.example.weaponMaster.api.slack.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReqSlackDto {

    private String  userId;
    private Byte    noticeType;
    private String  channelId;
}