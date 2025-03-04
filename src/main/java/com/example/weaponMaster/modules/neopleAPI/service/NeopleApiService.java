package com.example.weaponMaster.modules.neopleAPI.service;

import com.example.weaponMaster.modules.common.dto.ApiResponse;
import com.example.weaponMaster.modules.neopleAPI.constant.NeopleApi;
import com.example.weaponMaster.modules.neopleAPI.util.UrlUtil;
import com.example.weaponMaster.api.neople.dto.RespAuctionDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class NeopleApiService {

    private final UrlUtil      urlUtil;
    private final RestClient   restClient = RestClient.create();
    private final ObjectMapper objectMapper;

    @SneakyThrows
    public ApiResponse<RespAuctionDto[]> searchAuction(String itemName) {
        ResponseEntity<String> response = restClient.get()
                .uri(urlUtil.getAuctionSearchUrl(itemName))
                .retrieve()
                .toEntity(String.class);

        // API 정상 통신 여부 확인
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new Exception("neople 경매 API 호출 실패: " + response.getStatusCode());
        }

        String   res      = response.getBody();
        JsonNode rootNode = objectMapper.readTree(res);
        JsonNode rowsNode = rootNode.path("rows");

        RespAuctionDto[] auctionArray = new RespAuctionDto[rowsNode.size()];
        for (int i = 0; i < rowsNode.size(); i++) {
            JsonNode row    = rowsNode.get(i);
            String   imgUrl = String.format(NeopleApi.ITEM_IMG_URL, row.path("itemId").asText());
            auctionArray[i] = new RespAuctionDto(imgUrl, row);
        }

        return ApiResponse.success(auctionArray);
    }
}
