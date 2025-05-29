package com.example.weaponMaster.modules.neopleAPI.service;

import com.example.weaponMaster.api.neople.dto.ReqAuctionDto;
import com.example.weaponMaster.api.neople.dto.RespUserAuctionDto;
import com.example.weaponMaster.common.util.ErrorUtils;
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
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.PeriodicTrigger;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
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
    public void resumeAuctionMonitoring() {
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

    public ApiResponse<RespAuctionDto[]> searchAuction(String itemName) {
        try {
            return handleSearchAuction(itemName);

        } catch (HttpServerErrorException e) {
            return handleSearchServerError(e);
        }
    }

    @SneakyThrows
    private ApiResponse<RespAuctionDto[]> handleSearchAuction(String itemName) {
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

        RespAuctionDto[] searchResults = new RespAuctionDto[rowsNode.size()];
        for (int i = 0; i < rowsNode.size(); i++) {
            JsonNode row     = rowsNode.get(i);
            String   imgUrl  = String.format(NeopleApi.ITEM_IMG_URL, row.path("itemId").asText());
            searchResults[i] = new RespAuctionDto(imgUrl, row);
        }

        return ApiResponse.success(searchResults);
    }

    private ApiResponse<RespAuctionDto[]> handleSearchServerError(HttpServerErrorException e) {
        if (e.getStatusCode() != HttpStatus.SERVICE_UNAVAILABLE) {
            throw e;
        }

        if (ErrorUtils.isErrorCode(e.getResponseBodyAsString(), "DNF980")) {
            String errMsg = "[ 던전앤파이터 시스템 점검 중 ]\n" +
                    "\n" +
                    "현재 던전앤파이터 시스템 점검으로 인해\n" +
                    "아이템 검색이 불가능합니다.\n" +
                    "공식홈페이지에서 점검시간을 확인해 주세요.";
            return ApiResponse.error(errMsg);
        }

        throw e;
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
    @SneakyThrows
    private void monitorAuction(UserAuctionNotice userNotice) {
        try {
            if (userNotice.getAuctionState() != AuctionState.SELLING) {
                stopMonitoring(userNotice.getId());
                return;
            }

            // TODO 잘 동작하는지 체크해 보기
            // 허용 가능할 때까지 최대 timeoutDuration 만큼 대기
            RateLimiter.waitForPermission(neopleApiRateLimiter);

            // 기존 정보가 조회되지 않는 404 에러로 판매 상태 변경 판단
            Retry.decorateCheckedRunnable(neopleApiRetry, () -> {
                checkAuctionState(userNotice);
            }).run();

        } catch (HttpClientErrorException e) {
            handleAuctionState(userNotice, e);
        } catch(HttpServerErrorException e) {
            handleMonitorServerError(userNotice, e);
        } catch (Exception e) {
            handleMonitorError(userNotice, e);
        }
    }

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

        if (!ErrorUtils.isErrorCode(e.getResponseBodyAsString(), "DNF004")) {
            throw e;
        }

        if (isExpired(userNotice)) {
            handleExpired(userNotice);
            return;
        }


        handleSoldOut(userNotice);
        return;
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

        String itemName   = userNotice.getItemInfo().path("itemName").asText();
        String expireDate = userNotice.getItemInfo().path("expireDate").asText();
        int    count      = userNotice.getItemInfo().path("count").asInt();
        long   unitPrice  = userNotice.getItemInfo().path("unitPrice").asLong();
        long   price      = count * unitPrice;

        String message = String.format(
                "`판매 기간 만료 알림`\n" +
                        "```\n" +
                        "아이템명: %s%s\n" +
                        "만료시각: %s\n" +
                        "등록금액: %s 골드%s\n" +
                        "```",
                itemName, getCountText(count),
                expireDate,
                formatPrice(price), getUnitPriceText(unitPrice, count)
        );

        slackService.sendMessage(userNotice.getUserId(), UserSlackNoticeType.WEAPON_MASTER_SERVICE_ALERT, message);
        sendAuctionStateChange(userNotice);
        stopMonitoring(userNotice.getId());
    }

    private String getUnitPriceText(long unitPrice, int count) {
        String unitPriceText = "";
        if (count > 1) {
            unitPriceText  = String.format(" (개당 %s)", formatPrice(unitPrice));
        }

        return unitPriceText;
    }

    private String getCountText(int count) {
        String countText = "";
        if (count > 1) {
            countText  = String.format(" %d개", count);
        }

        return countText;
    }

    private void handleSoldOut(UserAuctionNotice userNotice) {
        userNotice.setAuctionState(AuctionState.SOLD_OUT);
        userAuctionNoticeRepo.save(userNotice);

        final long   DEPOSIT        = 10_000L; // 보증금 10,000 골드 고정
        final double SALES_FEE_RATE = 0.03;    // 수수료 3%

        String itemName  = userNotice.getItemInfo().path("itemName").asText();
        int    count     = userNotice.getItemInfo().path("count").asInt();
        long   unitPrice = userNotice.getItemInfo().path("unitPrice").asLong();
        long   price     = count * unitPrice;
        long   salesFee  = Math.round(price * SALES_FEE_RATE);
        long   amount    = price - salesFee + DEPOSIT;

        String message = String.format(
                "`판매 완료 알림`\n" +
                        "```\n" +
                        "[%s이(가)%s 판매되었습니다.]\n" +
                        "판매가: + %s 골드%s\n" +
                        "보증금: + %s 골드\n" +
                        "수수료: - %s 골드\n" +
                        "\n" +
                        "최종 정산 금액은 %s 골드입니다." +
                        "```",
                itemName, getCountText(count),
                formatPrice(price), getUnitPriceText(unitPrice, count),
                formatPrice(DEPOSIT),
                formatPrice(salesFee),
                formatPrice(amount)
        );

        slackService.sendMessage(userNotice.getUserId(), UserSlackNoticeType.WEAPON_MASTER_SERVICE_ALERT, message);
        sendAuctionStateChange(userNotice);
        stopMonitoring(userNotice.getId());
    }

    // 숫자 3자리마다 콤마 찍기
    private String formatPrice(long price) {
        return String.format("%,d", price);
    }

    private void handleMonitorServerError(UserAuctionNotice userNotice, HttpServerErrorException e) {
        if (e.getStatusCode() != HttpStatus.SERVICE_UNAVAILABLE) {
            throw e;
        }

        if (ErrorUtils.isErrorCode(e.getResponseBodyAsString(), "DNF980")) {
            handleServerInspect(userNotice);
            return;
        }

        throw e;
    }

    private void handleServerInspect(UserAuctionNotice userNotice) {
        userNotice.setAuctionState(AuctionState.SERVER_INSPECT);
        userAuctionNoticeRepo.save(userNotice);

        String noticeUrl = "https://df.nexon.com/community/news/notice/list?categoryType=1";
        String itemName  = userNotice.getItemInfo().path("itemName").asText();
        int    count     = userNotice.getItemInfo().path("count").asInt();
        long   unitPrice = userNotice.getItemInfo().path("unitPrice").asLong();
        long   price     = count * unitPrice;

        String message = String.format(
                "`판매 상태 추적 실패` - <%s|점검 공지사항 확인하기>\n" +
                        "```\n" +
                        "[던전앤파이터 시스템 점검으로 인해 현재 판매 상태를 추적할 수 없습니다.]\n" +
                        "[점검이 종료된 후 다시 등록해 주시기 바랍니다.]\n" +
                        "\n" +
                        "아이템: %s%s\n" +
                        "판매가: %s 골드%s\n" +
                        "```",
                noticeUrl,
                itemName, getCountText(count),
                formatPrice(price), getUnitPriceText(unitPrice, count)
        );

        slackService.sendMessage(userNotice.getUserId(), UserSlackNoticeType.WEAPON_MASTER_SERVICE_ALERT, message);
        sendAuctionStateChange(userNotice);
        stopMonitoring(userNotice.getId());
    }

    private void handleMonitorError(UserAuctionNotice userNotice, Exception e) {
        userNotice.setAuctionState(AuctionState.MONITOR_ERROR);
        userAuctionNoticeRepo.save(userNotice);

        Throwable root = ExceptionUtils.getRootCause(e);
        if (root == null) {
            root = e;
        }

        String errMessage = String.format(
                "`[경매 판매 알림 추적 에러]`\n" +
                        "```\n" +
                        "유저 ID: %s\n" +
                        "추적 ID: %d\n" +
                        "\n" +
                        "예외 타입: %s\n" +
                        "에러 메시지: %s\n" +
                        "\n" +
                        "루트 예외 타입: %s\n" +
                        "루트 메시지: %s\n" +
                        "```",
                userNotice.getUserId(),
                userNotice.getId(),
                e.getMessage(),
                e.getClass().getName(),
                root.getClass().getName(),
                root.getMessage()
        );

        System.err.println(errMessage);
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
