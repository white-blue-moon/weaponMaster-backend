package com.example.weaponMaster.modules.neopleAPI.service;

import com.example.weaponMaster.api.neople.dto.ReqAuctionDto;
import com.example.weaponMaster.api.neople.dto.RespUserAuctionDto;
import com.example.weaponMaster.modules.common.dto.ApiResponse;
import com.example.weaponMaster.modules.neopleAPI.constant.AuctionNotice;
import com.example.weaponMaster.modules.neopleAPI.constant.AuctionState;
import com.example.weaponMaster.modules.neopleAPI.constant.NeopleApi;
import com.example.weaponMaster.modules.neopleAPI.entity.UserAuctionNotice;
import com.example.weaponMaster.modules.neopleAPI.repository.UserAuctionNoticeRepository;
import com.example.weaponMaster.modules.neopleAPI.util.UrlUtil;
import com.example.weaponMaster.api.neople.dto.RespAuctionDto;
import com.example.weaponMaster.modules.slack.constant.UserSlackType;
import com.example.weaponMaster.modules.slack.service.SlackNotifier;
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
import java.util.Arrays;
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
    private final SlackNotifier slackNotifier;

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

    // 경매 판매 알림 등록
    public ApiResponse<Void> registerAuctionNotice(ReqAuctionDto request) {
        // 1. 현재 SELLING 추적 중인 알람 개수 확인
        UserAuctionNotice[] userNotices = userAuctionNoticeRepo.findByUserIdAndState(request.getUserId(), AuctionState.SELLING);
        if (userNotices.length >= AuctionNotice.MAX_NOTICE_COUNT) {
            String errMsg = String.format("[알람 최대 등록 가능 개수 초과] userId: %s, maxNoticeCount: %d, userSellingCount: %d", request.getUserId(), AuctionNotice.MAX_NOTICE_COUNT, userNotices.length);
            throw new IllegalArgumentException(errMsg);
        }

        // 2. DB 에 경매 판매 알림 정보 저장
        UserAuctionNotice notice = new UserAuctionNotice(request.getUserId(), request.getItemImg(), request.getItemInfo());
        userAuctionNoticeRepo.save(notice);

        // 3. 판매 상태 1분 마다 확인하는 스케줄 등록
        UserAuctionNotice  userNotice = userAuctionNoticeRepo.findByUserIdAndNo(notice.getUserId(), notice.getAuctionNo());
        ScheduledFuture<?> future     = taskScheduler.schedule(() -> monitorAuction(userNotice), new PeriodicTrigger(Duration.ofMinutes(1)));
        if (future == null) {
            throw new RuntimeException(String.format("[경매 알람 추적 스케줄 등록 실패] userId: %s", notice.getUserId()));
        }

        // 4. 등록한 스케줄을 맵에 저장하여 관리
        auctionMonitorMap.put(userNotice.getId(), future);
        return ApiResponse.success();
    }

    // TODO 서버 통신이 끊겼다가 다시 연결될 때 DB 에서 SELLING 상태의 알림은 다시 1분 마다 추적하도록 세팅 필요
    private void monitorAuction(UserAuctionNotice userNotice) {
        try {
            if (userNotice.getAuctionState() != AuctionState.SELLING) {
                stopMonitoring(userNotice.getId());
                return;
            }

            String            expireDateStr = userNotice.getItemInfo().path("expireDate").asText();
            DateTimeFormatter formatter     = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime     expireTime    = LocalDateTime.parse(expireDateStr, formatter);
            LocalDateTime     now           = LocalDateTime.now();

            if (now.isEqual(expireTime) || now.isAfter(expireTime)) {
                userNotice.setAuctionState(AuctionState.EXPIRED);
                userAuctionNoticeRepo.save(userNotice);

                String message = "[판매 기간 만료 알림] \n";
                message += userNotice.getItemInfo().path("itemName").asText() + " 의 판매 기간이 만료되었습니다. \n";
                message += "판매 만료 시각 : " + userNotice.getItemInfo().path("expireDate").asText();
                slackNotifier.sendMessage(userNotice.getUserId(), UserSlackType.AUCTION_NOTICE, message);

                stopMonitoring(userNotice.getId());
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

                    // TODO 슬랙 알림 외에 웨펀마스터 쪽지 함에도 알림 가도록 하는 방식도 고려해 보기 (쪽지함 만들지 고려 중)
                    if ("DNF004".equals(errorCode)) {
                        userNotice.setAuctionState(AuctionState.SOLD_OUT);
                        userAuctionNoticeRepo.save(userNotice);

                        // TODO regCount 로 계산해서 정보 알리도록 수정 필요
                        // TODO 10,000 골드 보증금 합산 및 수수료 제외한 가격으로도 알리기
                        String priceStr       = userNotice.getItemInfo().path("currentPrice").asText();
                        String formattedPrice = priceStr.replaceAll("(\\d)(?=(\\d{3})+$)", "$1,");
                        String itemName       = userNotice.getItemInfo().path("itemName").asText();
                        String message = String.format(
                                "`[판매 완료 알림]`\n" +
                                        "```\n" +
                                        "아이템명: %s\n" +
                                        "판매가격: %s G\n" +
                                        "```",
                                itemName,
                                formattedPrice
                        );
                        slackNotifier.sendMessage(userNotice.getUserId(), UserSlackType.AUCTION_NOTICE, message);

                        stopMonitoring(userNotice.getId());
                        return;
                    }
                }
                throw e;
            }

            // TODO 최신 경매 정보로 DB 알림 update 필요 (몇 개 남았는지 등)

        } catch (Exception e) {
            System.err.println("Auction monitoring error: " + e.getMessage());
            stopMonitoring(userNotice.getId()); // 에러 발생 시에도 추적 종료
            return;
        }
    }

    private void stopMonitoring(Integer id) {
        ScheduledFuture<?> future = auctionMonitorMap.remove(id); // 맵에서 제거
        if (future != null) {
            future.cancel(true); // 실행 중인 작업이 있으면 강제 중단
        }
    }

    public ApiResponse<RespUserAuctionDto> getAuctionNotice(String userId) {
        UserAuctionNotice[] userNoticeList = userAuctionNoticeRepo.findByUserId(userId);
        RespAuctionDto[]    noticeList     = Arrays.stream(userNoticeList)
                .map(userNotice -> new RespAuctionDto(
                        userNotice.getItemImg(),
                        userNotice.getItemInfo(),
                        userNotice.getAuctionState()
                ))
                .toArray(RespAuctionDto[]::new);

        RespUserAuctionDto respDto = new RespUserAuctionDto(AuctionNotice.MAX_NOTICE_COUNT, noticeList);
        return ApiResponse.success(respDto);
    }

    // 경매 판매 알림 해제
    public ApiResponse<Void> removeAuctionNotice(ReqAuctionDto request) {
        String            auctionNo  = request.getItemInfo().path("auctionNo").asText();
        UserAuctionNotice userNotice = userAuctionNoticeRepo.findByUserIdAndNo(request.getUserId(), auctionNo);

        // 1. DB 에 등록된 알림 정보 삭제
        userAuctionNoticeRepo.deleteById(userNotice.getId());

        // 2. 맵에 등록된 알림 정보 삭제
        stopMonitoring(userNotice.getId());
        return ApiResponse.success();
    }

}
