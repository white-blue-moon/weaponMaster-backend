package com.example.weaponMaster.api.neople.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class RespUserAuctionDto {

    private final Integer          maxNoticeCount;
    private final RespAuctionDto[] noticeList;
}
