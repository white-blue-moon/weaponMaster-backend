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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.PeriodicTrigger;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

@Service
@RequiredArgsConstructor
public class NeopleApiService {

    private final UrlUtil                                        urlUtil;
    private final RestClient                                     restClient = RestClient.create();
    private final ObjectMapper                                   objectMapper;
    private final UserAuctionNoticeRepository                    userAuctionNoticeRepo;
    private final TaskScheduler                                  taskScheduler;
    private final ConcurrentHashMap<Integer, ScheduledFuture<?>> auctionMonitorMap = new ConcurrentHashMap<>(); // 추적 중인 경매 판매 알림을 관리하는 맵

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
    // 경매 판매 알림 등록
    public ApiResponse<Void> registerAuctionNotice(ReqAuctionDto request) {
        // 1. DB 에 경매 판매 알림 정보 저장
        UserAuctionNotice notice = new UserAuctionNotice(request.getUserId(), request.getItemImg(), request.getItemInfo());
        userAuctionNoticeRepo.save(notice);

        // 2. 판매 상태 1분 마다 확인하는 스케줄 등록
        UserAuctionNotice userNotice = userAuctionNoticeRepo.findByUserIdAndNo(notice.getUserId(), notice.getAuctionNo());
        ScheduledFuture<?> future = taskScheduler.schedule(
                () -> monitorAuction(userNotice),
                new PeriodicTrigger(Duration.ofMinutes(1))
        );

        // TODO 에러 처리 중앙에서만 하도록 할 수는 없는지 확인해 보기
        // if (future == null) {
        //     throw new Exception("경매 판매 상태 추적 알림 등록 실패");
        // }

        // 3. 등록한 스케줄을 맵에 저장하여 관리
        auctionMonitorMap.put(userNotice.getId(), future);

        return ApiResponse.success();
    }

    // 경매 판매 알림 해제
    public ApiResponse<Void> removeAuctionNotice(ReqAuctionDto request) {
        String            auctionNo  = request.getItemInfo().path("auctionNo").asText();
        UserAuctionNotice userNotice = userAuctionNoticeRepo.findByUserIdAndNo(request.getUserId(), auctionNo);

        // 1. 맵에 등록된 알림 정보 삭제
        auctionMonitorMap.remove(userNotice.getId());

        // 2. DB 에 등록된 알림 정보 삭제
        userAuctionNoticeRepo.deleteById(userNotice.getId());

        return ApiResponse.success();
    }

    // TODO 서버 통신이 끊겼다가 다시 연결될 때 DB 에서 SELLING 상태의 알림은 다시 1분 마다 추적하도록 세팅 필요
    private void monitorAuction(UserAuctionNotice userNotice) {
        try {
            if (userNotice.getAuctionState() != AuctionState.SELLING) {
                return;
            }

            String            expireDateStr = userNotice.getItemInfo().path("expireDate").asText();
            DateTimeFormatter formatter     = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime     expireTime    = LocalDateTime.parse(expireDateStr, formatter);
            LocalDateTime     now           = LocalDateTime.now();

            System.out.println("[판매 알림 추적 중] (" + now + ")");
            System.out.println(userNotice.getItemInfo().path("itemName").asText() + "의 판매 상태를 확인합니다. " + "[#" + userNotice.getId() + "]");
            System.out.println();

            if (now.isEqual(expireTime) || now.isAfter(expireTime)) {
                userNotice.setAuctionState(AuctionState.EXPIRED);
                userAuctionNoticeRepo.save(userNotice);

                System.out.println("[판매 기간 만료 알림] (" + now + ")");
                System.out.println(userNotice.getItemInfo().path("itemName").asText() + " 의 판매 기간이 만료되었습니다.");
                System.out.println("판매 만료 시각 : " + userNotice.getItemInfo().path("expireDate").asText());
                System.out.println();
                return;
            }

            ResponseEntity<String> response = null;
            try {
                response = restClient.get()
                        .uri(urlUtil.getAuctionNoSearchUrl(userNotice.getAuctionNo()))
                        .retrieve()
                        .toEntity(String.class);
            } catch (HttpClientErrorException e) {
                if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                    String   errorBody = e.getResponseBodyAsString();
                    JsonNode rootNode  = objectMapper.readTree(errorBody);
                    String   errorCode = rootNode.path("error").path("code").asText();

                    if ("DNF004".equals(errorCode)) {
                        userNotice.setAuctionState(AuctionState.SOLD_OUT);
                        userAuctionNoticeRepo.save(userNotice);

                        String priceStr       = userNotice.getItemInfo().path("currentPrice").asText();
                        String formattedPrice = priceStr.replaceAll("(\\d)(?=(\\d{3})+$)", "$1,");
                        System.err.println("[판매 완료 알림] (" + now + ")");
                        System.err.println(userNotice.getItemInfo().path("itemName").asText() + " 이 " + formattedPrice + " G 에 판매 완료되었습니다.");
                        System.err.println();
                        return;
                    }
                }
                throw e;
            }

        } catch (Exception e) {
            System.err.println("Auction monitoring error: " + e.getMessage());
            return;
        }
    }

}
