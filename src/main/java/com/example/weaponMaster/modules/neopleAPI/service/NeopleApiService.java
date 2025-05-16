package com.example.weaponMaster.modules.neopleAPI.service;

import com.example.weaponMaster.api.neople.dto.ReqAuctionDto;
import com.example.weaponMaster.api.neople.dto.RespUserAuctionDto;
import com.example.weaponMaster.modules.account.constant.LogActType;
import com.example.weaponMaster.modules.account.constant.LogContentsType;
import com.example.weaponMaster.modules.account.service.UserLogService;
import com.example.weaponMaster.modules.common.dto.ApiResponse;
import com.example.weaponMaster.modules.neopleAPI.constant.AuctionNotice;
import com.example.weaponMaster.modules.neopleAPI.constant.AuctionState;
import com.example.weaponMaster.modules.neopleAPI.constant.NeopleApi;
import com.example.weaponMaster.modules.neopleAPI.entity.UserAuctionNotice;
import com.example.weaponMaster.modules.neopleAPI.repository.UserAuctionNoticeRepository;
import com.example.weaponMaster.modules.neopleAPI.util.UrlUtil;
import com.example.weaponMaster.api.neople.dto.RespAuctionDto;
import com.example.weaponMaster.modules.slack.constant.AdminSlackChannelType;
import com.example.weaponMaster.modules.slack.constant.UserSlackNoticeType;
import com.example.weaponMaster.modules.slack.service.SlackService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.retry.Retry;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
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

    private final UrlUtil                       urlUtil;
    private final RestClient                    restClient;
    private final ObjectMapper                  objectMapper;
    private final UserAuctionNoticeRepository   userAuctionNoticeRepo;
    private final TaskScheduler                 taskScheduler;
    private final SlackService                  slackService;
    private final UserLogService                userLogService;
    private final SimpMessagingTemplate         messagingTemplate;

    private final ConcurrentHashMap<Integer, ScheduledFuture<?>> auctionMonitorMap = new ConcurrentHashMap<>(); // 추적 중인 경매 판매 알림을 관리하는 맵

    private final RateLimiter neopleApiRateLimiter;
    private final Retry       neopleApiRetry;


    // 서버 재시작 후 SELLING 상태 알림들 스케줄 재등록
    @PostConstruct
    public void continueMonitorAuction() {
        // TODO AuctionState.MONITOR_ERROR 는 어떻게 처리할지 고민
        UserAuctionNotice[] sellingNotices = userAuctionNoticeRepo.findByState(AuctionState.SELLING);

        for (UserAuctionNotice userNotice : sellingNotices) {
            if (!auctionMonitorMap.containsKey(userNotice.getId())) {
                ScheduledFuture<?> future = taskScheduler.schedule(() -> monitorAuction(userNotice), new PeriodicTrigger(Duration.ofMinutes(1)));
                if (future == null) {
                    throw new RuntimeException(String.format("[경매 알람 추적 스케줄 재등록 실패] userId: %s", userNotice.getUserId()));
                }

                auctionMonitorMap.put(userNotice.getId(), future);
                userLogService.saveLog(userNotice.getUserId(), false, LogContentsType.AUCTION_NOTICE, LogActType.CONTINUE, (short)(int)userNotice.getId());
            }
        }
    }

    @SneakyThrows
    public ApiResponse<RespAuctionDto[]> searchAuction(String itemName) {
        ResponseEntity<String> response = restClient.get()
                .uri(urlUtil.getAuctionSearchUrl(itemName))
                .retrieve()
                .toEntity(String.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("neople 경매 아이템 명 검색 API 호출 실패: " + response.getStatusCode());
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

        userLogService.saveLog(request.getUserId(), request.getIsAdminMode(), LogContentsType.AUCTION_NOTICE, LogActType.CREATE, (short)(int)userNotice.getId());
        return ApiResponse.success();
    }

    // 스레드 풀에서의 에러는 글로벌 에러에서 잡지 못하므로 별도 try, catch 필요
    private void monitorAuction(UserAuctionNotice userNotice) {
        try {
            if (userNotice.getAuctionState() != AuctionState.SELLING) {
                stopMonitoring(userNotice.getId());
                return;
            }

            // 허용 가능할 때까지 최대 timeoutDuration 만큼 대기
            RateLimiter.waitForPermission(neopleApiRateLimiter);

            // 기존 정보가 조회되지 않는 404 에러로 판매 상태 변경 판단
            Retry.decorateRunnable(neopleApiRetry, () -> {
                checkAuctionState(userNotice);
            }).run();

        } catch (HttpClientErrorException e) {
            handleAuctionState(userNotice, e);
        } catch (Exception e) {
            handleMonitorError(userNotice, e);
        }
    }

    // 최대 재시도 횟수: 3회, 재시도 간격: 2000ms (2초) 간격으로 재시도
    public void checkAuctionState(UserAuctionNotice userNotice) {
        restClient.get()
                .uri(urlUtil.getAuctionNoSearchUrl(userNotice.getAuctionNo()))
                .retrieve()
                .toEntity(String.class);
    }

    private void handleAuctionState(UserAuctionNotice userNotice, HttpClientErrorException e) {
        if (e.getStatusCode() != HttpStatus.NOT_FOUND) {
            throw e;
        }

        if (!isCodeDNF004(e.getResponseBodyAsString())) {
            throw e;
        }

        if (isExpired(userNotice)) {
            handleExpired(userNotice);
            return;
        }

        handleSoldOut(userNotice);
        return;
    }

    @SneakyThrows
    private Boolean isCodeDNF004(String errorBody) {
        JsonNode rootNode  = objectMapper.readTree(errorBody);
        String   errorCode = rootNode.path("error").path("code").asText();

        if ("DNF004".equals(errorCode)) {
            return true;
        }

        return false;
    }

    private Boolean isExpired(UserAuctionNotice userNotice) {
        String            expireDateStr = userNotice.getItemInfo().path("expireDate").asText();
        DateTimeFormatter formatter     = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime     expireTime    = LocalDateTime.parse(expireDateStr, formatter);
        LocalDateTime     now           = LocalDateTime.now();

        if (now.isEqual(expireTime) || now.isAfter(expireTime)) {
            return true;
        }

        return false;
    }

    private void handleExpired(UserAuctionNotice userNotice) {
        userNotice.setAuctionState(AuctionState.EXPIRED);
        userAuctionNoticeRepo.save(userNotice);

        String itemName = userNotice.getItemInfo().path("itemName").asText();
        String message = String.format(
                "`판매 기간 만료 알림`\n" +
                        "```\n" +
                        "아이템명: %s\n" +
                        "만료시각: %s\n" +
                        "```",
                itemName,
                userNotice.getItemInfo().path("expireDate").asText()
        );

        slackService.sendMessage(userNotice.getUserId(), UserSlackNoticeType.WEAPON_MASTER_SERVICE_ALERT, message);
        sendAuctionStateChange(userNotice);
        stopMonitoring(userNotice.getId());
    }

    private void handleSoldOut(UserAuctionNotice userNotice) {
        userNotice.setAuctionState(AuctionState.SOLD_OUT);
        userAuctionNoticeRepo.save(userNotice);

        // TODO regCount 에 대한 최종 가격 정보 전달하기 (수수료, 보증금 모두 계산한 결과 전달하기), 아이템 개수 정보도 전달하기
        String priceStr       = userNotice.getItemInfo().path("currentPrice").asText();
        String formattedPrice = priceStr.replaceAll("(\\d)(?=(\\d{3})+$)", "$1,");
        String itemName       = userNotice.getItemInfo().path("itemName").asText();
        String message        = String.format(
                "`판매 완료 알림`\n" +
                        "```\n" +
                        "아이템명: %s\n" +
                        "판매가격: %s G\n" +
                        "```",
                itemName,
                formattedPrice
        );

        slackService.sendMessage(userNotice.getUserId(), UserSlackNoticeType.WEAPON_MASTER_SERVICE_ALERT, message);
        sendAuctionStateChange(userNotice);
        stopMonitoring(userNotice.getId());
    }

    private void handleMonitorError(UserAuctionNotice userNotice, Exception e) {
        userNotice.setAuctionState(AuctionState.MONITOR_ERROR);
        userAuctionNoticeRepo.save(userNotice);

        String errMessage = String.format(
                "`[경매 판매 알림 추적 에러]`\n" +
                        "```\n" +
                        "유저 ID: %s\n" +
                        "추적 ID: %d\n" +
                        "에러: %s\n" +
                        "```",
                userNotice.getUserId(),
                userNotice.getId(),
                e.getMessage()
        );

        System.err.println(e.getMessage());
        slackService.sendMessageAdmin(AdminSlackChannelType.BACK_END_ERROR_NOTICE, errMessage);
        stopMonitoring(userNotice.getId());
    }

    // TODO 경로 상수화 하기
    // 프론트가 구독 중인 topic 으로 메시지를 보냄 (WebSocket 통신)
    private void sendAuctionStateChange(UserAuctionNotice userNotice) {
        messagingTemplate.convertAndSend(
                "/topic/auction-state",
                new RespAuctionDto(
                        userNotice.getItemImg(),
                        userNotice.getItemInfo(),
                        userNotice.getAuctionState())
        );
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

        userLogService.saveLog(request.getUserId(), request.getIsAdminMode(), LogContentsType.AUCTION_NOTICE, LogActType.DELETE, (short)(int)userNotice.getId());
        return ApiResponse.success();
    }

}
