package com.vovgoo.BankSystem.controller;

import com.vovgoo.BankSystem.dto.account.request.CreateAccountRequest;
import com.vovgoo.BankSystem.dto.account.request.DepositAccountRequest;
import com.vovgoo.BankSystem.dto.account.request.TransferAccountRequest;
import com.vovgoo.BankSystem.dto.account.request.WithdrawAccountRequest;
import com.vovgoo.BankSystem.dto.transaction.TransactionResponse;
import com.vovgoo.BankSystem.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@Tag(name = "Счёта", description = "Операции по счетам")
@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @Operation(
            summary = "Создать счёт",
            description = "Создаёт новый банковский счёт для клиента",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CreateAccountRequest.class),
                            examples = @ExampleObject(value = "{ \"clientId\": \"123e4567-e89b-12d3-a456-426614174000\" }")
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Счёт успешно создан",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TransactionResponse.class),
                                    examples = @ExampleObject(value = "{ \"status\": \"APPROVED\", \"message\": \"Счёт успешно создан\", \"timestamp\": \"2025-11-01T12:34:56\" }")
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "Некорректные данные запроса",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TransactionResponse.class),
                                    examples = @ExampleObject(value = "{ \"status\": \"DECLINED\", \"message\": \"Некорректные данные запроса\", \"timestamp\": \"2025-11-01T12:34:57\" }")
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Клиент не найден",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TransactionResponse.class),
                                    examples = @ExampleObject(value = "{ \"status\": \"DECLINED\", \"message\": \"Клиент не найден\", \"timestamp\": \"2025-11-01T12:34:58\" }")
                            )
                    ),
                    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервиса",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TransactionResponse.class),
                                    examples = @ExampleObject(value = "{ \"status\": \"DECLINED\", \"message\": \"Внутренняя ошибка сервиса\", \"timestamp\": \"2025-11-01T12:34:59\" }")
                            )
                    )
            }
    )
    @PostMapping
    public ResponseEntity<TransactionResponse> create(@RequestBody @Valid CreateAccountRequest request) {
        log.info("Создание счёта для клиента: clientId={}", request.clientId());
        return ResponseEntity.status(HttpStatus.CREATED).body(accountService.create(request));
    }

    @Operation(
            summary = "Удалить счёт",
            description = "Удаляет счёт по ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Счёт успешно удалён",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TransactionResponse.class),
                                    examples = @ExampleObject(value = "{ \"status\": \"APPROVED\", \"message\": \"Счёт успешно удалён\", \"timestamp\": \"2025-11-01T12:35:00\" }")
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "Невозможно удалить счёт с ненулевым балансом",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TransactionResponse.class),
                                    examples = @ExampleObject(value = "{ \"status\": \"DECLINED\", \"message\": \"Невозможно удалить счёт с ненулевым балансом\", \"timestamp\": \"2025-11-01T12:35:01\" }")
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Счёт не найден",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TransactionResponse.class),
                                    examples = @ExampleObject(value = "{ \"status\": \"DECLINED\", \"message\": \"Счёт не найден\", \"timestamp\": \"2025-11-01T12:35:02\" }")
                            )
                    ),
                    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервиса",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TransactionResponse.class),
                                    examples = @ExampleObject(value = "{ \"status\": \"DECLINED\", \"message\": \"Внутренняя ошибка сервиса\", \"timestamp\": \"2025-11-01T12:35:03\" }")
                            )
                    )
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<TransactionResponse> delete(@PathVariable UUID id) {
        log.info("Удаление счёта: accountId={}", id);
        return ResponseEntity.ok(accountService.delete(id));
    }

    @Operation(
            summary = "Пополнить счёт",
            description = "Внести средства на счёт",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = DepositAccountRequest.class),
                            examples = @ExampleObject(value = "{ \"accountId\": \"123e4567-e89b-12d3-a456-426614174001\", \"amount\": 1000.00 }")
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Счёт успешно пополнен",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TransactionResponse.class),
                                    examples = @ExampleObject(value = "{ \"status\": \"APPROVED\", \"message\": \"Счёт успешно пополнен\", \"timestamp\": \"2025-11-01T12:36:00\" }")
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "Некорректные данные запроса",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TransactionResponse.class),
                                    examples = @ExampleObject(value = "{ \"status\": \"DECLINED\", \"message\": \"Некорректные данные запроса\", \"timestamp\": \"2025-11-01T12:36:01\" }")
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Счёт не найден",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TransactionResponse.class),
                                    examples = @ExampleObject(value = "{ \"status\": \"DECLINED\", \"message\": \"Счёт не найден\", \"timestamp\": \"2025-11-01T12:36:02\" }")
                            )
                    ),
                    @ApiResponse(responseCode = "409", description = "Конфликт при параллельной операции (Optimistic Lock)",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TransactionResponse.class),
                                    examples = @ExampleObject(value = "{ \"status\": \"DECLINED\", \"message\": \"Конфликт при параллельной операции. Попробуйте повторить действие.\", \"timestamp\": \"2025-11-01T12:36:05\" }")
                            )
                    ),
                    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервиса",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TransactionResponse.class),
                                    examples = @ExampleObject(value = "{ \"status\": \"DECLINED\", \"message\": \"Внутренняя ошибка сервиса\", \"timestamp\": \"2025-11-01T12:36:03\" }")
                            )
                    )
            }
    )
    @PostMapping("/deposit")
    public ResponseEntity<TransactionResponse> deposit(@RequestBody @Valid DepositAccountRequest request) {
        log.info("Пополнение счёта: accountId={}, amount={}", request.accountId(), request.amount());
        return ResponseEntity.ok(accountService.deposit(request));
    }

    @Operation(
            summary = "Снять средства",
            description = "Снять средства со счёта",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = WithdrawAccountRequest.class),
                            examples = @ExampleObject(value = "{ \"accountId\": \"123e4567-e89b-12d3-a456-426614174001\", \"amount\": 500.00 }")
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Снятие прошло успешно",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TransactionResponse.class),
                                    examples = @ExampleObject(value = "{ \"status\": \"APPROVED\", \"message\": \"Снятие прошло успешно\", \"timestamp\": \"2025-11-01T12:37:00\" }")
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "Недостаточно средств или некорректные данные",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TransactionResponse.class),
                                    examples = @ExampleObject(value = "{ \"status\": \"DECLINED\", \"message\": \"Недостаточно средств или некорректные данные\", \"timestamp\": \"2025-11-01T12:37:01\" }")
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Счёт не найден",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TransactionResponse.class),
                                    examples = @ExampleObject(value = "{ \"status\": \"DECLINED\", \"message\": \"Счёт не найден\", \"timestamp\": \"2025-11-01T12:37:02\" }")
                            )
                    ),
                    @ApiResponse(responseCode = "409", description = "Конфликт при параллельной операции (Optimistic Lock)",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TransactionResponse.class),
                                    examples = @ExampleObject(value = "{ \"status\": \"DECLINED\", \"message\": \"Конфликт при параллельной операции. Попробуйте повторить действие.\", \"timestamp\": \"2025-11-01T12:36:05\" }")
                            )
                    ),
                    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервиса",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TransactionResponse.class),
                                    examples = @ExampleObject(value = "{ \"status\": \"DECLINED\", \"message\": \"Внутренняя ошибка сервиса\", \"timestamp\": \"2025-11-01T12:37:03\" }")
                            )
                    )
            }
    )
    @PostMapping("/withdraw")
    public ResponseEntity<TransactionResponse> withdraw(@RequestBody @Valid WithdrawAccountRequest request) {
        log.info("Снятие со счёта: accountId={}, amount={}", request.accountId(), request.amount());
        return ResponseEntity.ok(accountService.withdraw(request));
    }

    @Operation(
            summary = "Перевести средства",
            description = "Перевести средства между счетами с учётом комиссии",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TransferAccountRequest.class),
                            examples = @ExampleObject(value = "{ \"fromAccountId\": \"123e4567-e89b-12d3-a456-426614174001\", \"toAccountId\": \"123e4567-e89b-12d3-a456-426614174002\", \"amount\": 200.00 }")
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Перевод успешно выполнен",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TransactionResponse.class),
                                    examples = @ExampleObject(value = "{ \"status\": \"APPROVED\", \"message\": \"Перевод успешно выполнен\", \"timestamp\": \"2025-11-01T12:38:00\" }")
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "Ошибка перевода: недостаточно средств, некорректные данные, перевод на тот же счёт",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TransactionResponse.class),
                                    examples = @ExampleObject(value = "{ \"status\": \"DECLINED\", \"message\": \"Ошибка перевода: недостаточно средств, некорректные данные или перевод на тот же счёт\", \"timestamp\": \"2025-11-01T12:38:01\" }")
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Один из счетов не найден",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TransactionResponse.class),
                                    examples = @ExampleObject(value = "{ \"status\": \"DECLINED\", \"message\": \"Один из счетов не найден\", \"timestamp\": \"2025-11-01T12:38:02\" }")
                            )
                    ),
                    @ApiResponse(responseCode = "409", description = "Конфликт при параллельной операции (Optimistic Lock)",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TransactionResponse.class),
                                    examples = @ExampleObject(value = "{ \"status\": \"DECLINED\", \"message\": \"Конфликт при параллельной операции. Попробуйте повторить действие.\", \"timestamp\": \"2025-11-01T12:36:05\" }")
                            )
                    ),
                    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервиса",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TransactionResponse.class),
                                    examples = @ExampleObject(value = "{ \"status\": \"DECLINED\", \"message\": \"Внутренняя ошибка сервиса\", \"timestamp\": \"2025-11-01T12:38:03\" }")
                            )
                    )
            }
    )
    @PostMapping("/transfer")
    public ResponseEntity<TransactionResponse> transfer(@RequestBody @Valid TransferAccountRequest request) {
        log.info("Перевод между счетами: fromAccountId={}, toAccountId={}, amount={}",
                request.fromAccountId(), request.toAccountId(), request.amount());
        return ResponseEntity.ok(accountService.transfer(request));
    }
}