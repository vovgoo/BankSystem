package com.vovgoo.BankSystem.controller;

import com.vovgoo.BankSystem.dto.stats.StatsResponse;
import com.vovgoo.BankSystem.dto.transaction.TransactionResponse;
import com.vovgoo.BankSystem.service.StatsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "Статистика", description = "Статистика по системе")
@RestController
@RequestMapping("/api/v1/stats")
@RequiredArgsConstructor
public class StatsController {

    private final StatsService statsService;

    @Operation(
            summary = "Обзор статистики",
            description = "Получить общую статистику по системе: количество клиентов, количество счетов, суммарный баланс и т.д.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Статистика успешно получена",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = StatsResponse.class),
                                    examples = @ExampleObject(
                                            value = "{\n" +
                                                    "  \"totalClients\": 100,\n" +
                                                    "  \"clientsWithAccounts\": 80,\n" +
                                                    "  \"clientsWithoutAccounts\": 20,\n" +
                                                    "  \"totalAccounts\": 150,\n" +
                                                    "  \"totalBalance\": 125000.50,\n" +
                                                    "  \"averageBalance\": 833.33,\n" +
                                                    "  \"maxBalance\": 10000.00,\n" +
                                                    "  \"minBalance\": 10.00\n" +
                                                    "}"
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Внутренняя ошибка сервиса",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TransactionResponse.class),
                                    examples = @ExampleObject(
                                            value = "{\n" +
                                                    "  \"status\": \"DECLINED\",\n" +
                                                    "  \"message\": \"Внутренняя ошибка сервиса\",\n" +
                                                    "  \"timestamp\": \"2025-11-01T12:00:00\"\n" +
                                                    "}"
                                    )
                            )
                    )
            }
    )
    @GetMapping
    public ResponseEntity<StatsResponse> getOverview() {
        log.info("Запрос общей статистики по системе");
        return ResponseEntity.ok(statsService.getOverview());
    }
}