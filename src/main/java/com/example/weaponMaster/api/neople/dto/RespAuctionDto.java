package com.example.weaponMaster.api.neople.dto;

import com.example.weaponMaster.modules.neopleAPI.constant.AuctionState;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class RespAuctionDto {

    private final String   imgUrl;
    private final JsonNode itemInfo;
    private final Integer  auctionState;

    public RespAuctionDto(String imgUrl, JsonNode itemInfo) {
        this.imgUrl       = imgUrl;
        this.itemInfo     = itemInfo;
        this.auctionState = AuctionState.NONE;
    }
}
