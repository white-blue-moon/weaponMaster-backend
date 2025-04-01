package com.example.weaponMaster.modules.neopleAPI.service;

import com.example.weaponMaster.api.neople.dto.ReqAuctionDto;
import com.example.weaponMaster.modules.common.dto.ApiResponse;
import com.example.weaponMaster.modules.neopleAPI.constant.AuctionState;
import com.example.weaponMaster.modules.neopleAPI.constant.NeopleApi;
import com.example.weaponMaster.modules.neopleAPI.entity.UserAuctionNotice;
import com.example.weaponMaster.modules.neopleAPI.repository.UserAuctionNoticeRepository;
import com.example.weaponMaster.modules.neopleAPI.util.UrlUtil;
import com.example.weaponMaster.api.neople.dto.RespAuctionDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.PeriodicTrigger;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class NeopleApiService {

    private final UrlUtil                     urlUtil;
    private final RestClient                  restClient = RestClient.create();
    private final ObjectMapper                objectMapper;
    private final UserAuctionNoticeRepository userAuctionNoticeRepo;
    private final TaskScheduler               taskScheduler;

    public ApiResponse<RespAuctionDto[]> searchAuction(String itemName) throws Exception {
        ResponseEntity<String> response = restClient.get()
                .uri(urlUtil.getAuctionSearchUrl(itemName))
                .retrieve()
                .toEntity(String.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new Exception("neople 경매 아이템 명 검색 API 호출 실패: " + response.getStatusCode());
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

    // TODO 경매장 알림은 인당 최대 5개까지만 등록 가능하도록 제한하기
    public ApiResponse<Void> registerAuctionNotice(ReqAuctionDto request) {
        // 1. DB 에 알림 정보 등록
        UserAuctionNotice notice = new UserAuctionNotice(request.getUserId(), request.getItemInfo());
        userAuctionNoticeRepo.save(notice);

        // 2. 판매 상태 1분마다 확인
        taskScheduler.schedule(() -> monitorAuction(notice), new PeriodicTrigger(Duration.ofMinutes(1)));
        return ApiResponse.success();
    }

    private void monitorAuction(UserAuctionNotice notice) {
        try {
            UserAuctionNotice updatedNotice = userAuctionNoticeRepo.findByUserIdAndNo(notice.getUserId(), notice.getAuctionNo());
            if (updatedNotice.getAuctionState() != AuctionState.SELLING) {
                return;
            }

            String            expireDateStr = notice.getItemInfo().path("expireDate").asText();
            DateTimeFormatter formatter     = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime     expireTime    = LocalDateTime.parse(expireDateStr, formatter);
            LocalDateTime     now           = LocalDateTime.now();

            if (now.isEqual(expireTime) || now.isAfter(expireTime)) {
                updatedNotice.setAuctionState(AuctionState.EXPIRED);
                userAuctionNoticeRepo.save(updatedNotice);
                return;
            }

            ResponseEntity<String> response = restClient.get()
                    .uri(urlUtil.getAuctionNoSearchUrl(notice.getAuctionNo()))
                    .retrieve()
                    .toEntity(String.class);

            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new Exception("neople 경매 등록 번호 검색 API 호출 실패: " + response.getStatusCode());
            }

            String   res      = response.getBody();
            JsonNode rootNode = objectMapper.readTree(res);

            // 판매 만료 기간이 지나지 않은 상태에서 더 이상 검색되지 않는다면 판매 완료 처리
            if (rootNode.has("error") && "DNF004".equals(rootNode.path("error").path("code").asText())) {
                updatedNotice.setAuctionState(AuctionState.SOLD_OUT);
                userAuctionNoticeRepo.save(updatedNotice);
                return;
            }
        } catch (Exception e) {
            System.err.println("Auction monitoring error: " + e.getMessage());
        }
    }
}
