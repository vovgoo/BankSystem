package com.vovgoo.BankSystem.controller;

import com.vovgoo.BankSystem.dto.stats.StatsResponse;
import com.vovgoo.BankSystem.service.StatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/stats")
@RequiredArgsConstructor
public class StatsController {

    private final StatsService statsService;

    @GetMapping
    public StatsResponse getOverview() {
        return statsService.getOverview();
    }
}