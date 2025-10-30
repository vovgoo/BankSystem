package com.vovgoo.BankSystem.service.impl;

import com.vovgoo.BankSystem.dto.stats.StatsResponse;
import com.vovgoo.BankSystem.service.StatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {

    @Override
    public StatsResponse getOverview() {
        return null;
    }
}