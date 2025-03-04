package com.example.weaponMaster.api.neople.dto;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class RespAuctionDto {

    private final String   imgUrl;
    private final JsonNode itemInfo;
}
