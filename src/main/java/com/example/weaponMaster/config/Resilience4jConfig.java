package com.example.weaponMaster.config;

import io.github.resilience4j.ratelimiter.*;
import io.github.resilience4j.retry.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.time.Duration;
import java.util.function.Predicate;

@Configuration
public class Resilience4jConfig {

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

    @Bean
    public Predicate<Throwable> retryOnGoAwayIOException() {
        return ex -> {
            if (ex instanceof IOException) {
                String msg = ex.getMessage();
                return msg != null && msg.contains("GOAWAY received");
            }
            return false;
        };
    }

    // Retry: 최대 3회 재시도, 재시도 간격 2초, 네오플 API 호출 실패 시 재시도
    @Bean
    public Retry neopleApiRetry(Predicate<Throwable> retryOnGoAwayIOException) {
        return Retry.of("neopleApiRetry",
                RetryConfig.custom()
                        .maxAttempts(3)
                        .waitDuration(Duration.ofSeconds(2))
                        .retryOnException(retryOnGoAwayIOException)
                        .build());
    }
}
