package com.example.weaponMaster.modules.neopleAPI.util;

import com.example.weaponMaster.modules.neopleAPI.constant.NeopleApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Component  // Spring Bean으로 등록
public class UrlUtil {

    private final String apiKey;

    // 생성자에서 apiKey @Value 주입 (application.properties 값 참조)
    public UrlUtil(@Value("${neople.api.key}") String apiKey) {
        this.apiKey = apiKey;
    }

    // TODO 인코딩을 하지 않고 바로 넘겨야 결과 값을 받아와서 함수 사용 임시 보류
    private static String encodeURIComponent(String component)   {
        String result = null;

        try {
            result = URLEncoder.encode(component, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            result = component;
        }

        return result;
    }

    // 경매 아이템 이름 검색 API URL
    public String getAuctionSearchUrl(String itemName) {
        String  defaultWordType = "front"; // 동일 단어(match), 앞 단어 검색(front), 전문 검색(full)
        String  defaultSort     = "desc";  // 가장 최근 등록된 순으로 조회
        Integer defaultLimit    = 5;       // 5개 항목만 조회

        return String.format(
                NeopleApi.AUCTION_SEARCH_URL,
                itemName,
                defaultWordType,
                defaultSort,
                defaultLimit,
                apiKey
        );
    }

    // 경매 등록 번호로 검색 API URL
    public String getAuctionNoSearchUrl(String itemNo) {
        return String.format(
                NeopleApi.AUCTION_NO_SEARCH_URL,
                itemNo,
                apiKey
        );
    }
}
