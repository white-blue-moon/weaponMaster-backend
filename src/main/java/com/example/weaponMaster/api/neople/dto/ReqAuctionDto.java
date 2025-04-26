package com.example.weaponMaster.api.neople.dto;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReqAuctionDto {

    private Boolean  isAdminMode;
    private String   userId;
    private String   itemImg;
    private JsonNode itemInfo;
}
