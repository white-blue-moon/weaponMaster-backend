package com.example.weaponMaster.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;

@Configuration
@EnableRetry
public class SpringRetryConfig {
    // 특별한 설정 없으면 이대로 둬도 됨
}
