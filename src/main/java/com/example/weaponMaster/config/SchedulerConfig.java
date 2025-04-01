package com.example.weaponMaster.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
public class SchedulerConfig {

    @Bean
    public ThreadPoolTaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(10); // 스레드 수(corePoolSize)와 실행할 수 있는 작업 수는 1:1 관계가 아님
        scheduler.setThreadNamePrefix("AuctionMonitor-");
        scheduler.initialize();
        return scheduler;
    }
}
