package com.example.weaponMaster.config;

import io.github.resilience4j.ratelimiter.*;
import io.github.resilience4j.retry.*;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.time.Duration;
import java.util.function.Predicate;

@Configuration
public class Resilience4jConfig {

    private static final Logger log = LoggerFactory.getLogger(Resilience4jConfig.class);

    // 네오플 API 요청 허용량: 1초 최대 허용량(1,000건), 1분 최대 허용량(60,000건), 1시간 최대 허용량(3,600,000건)
    // 초당 1000건 이하로 분산 호출되도록 제어하며, 초과 시 최대 3초 대기 후 요청
    @Bean
    public RateLimiter neopleApiRateLimiter() {
        return RateLimiter.of("neopleApiRateLimiter",
                RateLimiterConfig.custom()
                        .limitForPeriod(1000)
                        .limitRefreshPeriod(Duration.ofSeconds(1))
                        .timeoutDuration(Duration.ofSeconds(3))
                        .build());
    }

    // 예외가 RuntimeException 등으로 감싸질 수 있으므로,
    // 근본 원인(root cause)인 IOException 을 확인하기 위해 cause 를 탐색함
    @Bean
    public Predicate<Throwable> retryOnIOException() {
        return ex -> {
            Throwable root = ExceptionUtils.getRootCause(ex);
            if (root == null) {
                root = ex;
            }

            // TODO 예외 타입 로그 확인용 임시 출력
            log.warn("[Exception chain]: ", ex);
            log.warn("[Root exception type]: {}", root.getClass().getName());
            log.warn("[Root exception message]: {}", root.getMessage());

            if (!(root instanceof IOException)) {
                return false;
            }

            String msg = root.getMessage();
            return msg != null && (msg.contains("GOAWAY received") || msg.contains("Broken pipe"));
        };
    }

    // Retry: 최대 3회 재시도, 재시도 간격 1초, 네오플 API 호출 실패 시 재시도
    @Bean
    public Retry neopleApiRetry(Predicate<Throwable> retryOnIOException) {
        RetryConfig config = RetryConfig.custom()
                .maxAttempts(3)
                .waitDuration(Duration.ofSeconds(1))
                .retryOnException(retryOnIOException)
                .build();

        Retry retry = Retry.of("neopleApiRetry", config);

        // TODO 실제 리트라이를 진행하는지 확인하기 위해 임시 로그 추가
        retry.getEventPublisher()
                .onRetry(event -> {
                    log.warn("[Retry] Attempt {} due to {}",
                            event.getNumberOfRetryAttempts(),
                            event.getLastThrowable() != null ? event.getLastThrowable().toString() : "unknown error");
                });

        return retry;
    }

}
