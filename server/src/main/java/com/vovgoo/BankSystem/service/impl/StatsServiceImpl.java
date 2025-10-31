package com.vovgoo.BankSystem.service.impl;

import com.vovgoo.BankSystem.dto.stats.StatsResponse;
import com.vovgoo.BankSystem.repository.StatsRepository;
import com.vovgoo.BankSystem.service.StatsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StatsServiceImpl implements StatsService {

    private final StatsRepository statsRepository;

    @Override
    public StatsResponse getOverview() {
        log.info("Запрос на получение общей статистики");
        StatsResponse statsResponse = statsRepository.getOverview();
        log.info("Статистика успешно получена: {}", statsResponse);
        return statsResponse;
    }
}