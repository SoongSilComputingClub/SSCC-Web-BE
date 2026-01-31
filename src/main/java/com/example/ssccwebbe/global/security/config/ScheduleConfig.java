package com.example.ssccwebbe.global.security.config;

import java.time.LocalDateTime;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.ssccwebbe.global.security.jwt.repository.PreUserRefreshRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ScheduleConfig {
    private final PreUserRefreshRepository preUserRefreshRepository;

    // 매일 3AM 마다 토큰 저장소에서 생성 이후 8일 지난 Refresh 토큰 삭제
    @Scheduled(cron = "0 0 3 * * *")
    public void refreshEntityTtlSchedule() {
        LocalDateTime cutoff = LocalDateTime.now().minusDays(8);
        preUserRefreshRepository.deleteByCreatedDateBefore(cutoff);
    }
}
