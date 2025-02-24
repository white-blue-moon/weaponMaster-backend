package com.example.weaponMaster.modules.neopleAPI.service;

import com.example.weaponMaster.modules.common.dto.ApiResponse;
import com.example.weaponMaster.modules.neopleAPI.util.UrlUtil;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class NeopleApiService {

    private final WebClient webClient;
    private final UrlUtil urlUtil;

    public NeopleApiService(WebClient.Builder webClientBuilder, UrlUtil urlUtil) {
        this.webClient = webClientBuilder.build();
        this.urlUtil = urlUtil;
    }

    public ApiResponse<String> searchAuction(String itemName) {
        String url = urlUtil.getAuctionSearchUrl(itemName);

        JsonNode jsonResponse =  webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();

        assert jsonResponse != null;
        String response = jsonResponse.toPrettyString();

        System.out.println("🔹 외부 API url: " + url); // 콘솔 출력
        System.out.println("🔹 외부 API 응답: " + response); // 콘솔 출력

        return ApiResponse.success(response);
    }
}
