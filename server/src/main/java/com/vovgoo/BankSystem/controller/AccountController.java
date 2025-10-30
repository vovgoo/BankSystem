package com.vovgoo.BankSystem.controller;

import com.vovgoo.BankSystem.dto.account.request.CreateAccountRequest;
import com.vovgoo.BankSystem.dto.account.request.DepositAccountRequest;
import com.vovgoo.BankSystem.dto.account.request.TransferAccountRequest;
import com.vovgoo.BankSystem.dto.account.request.WithdrawAccountRequest;
import com.vovgoo.BankSystem.dto.transaction.TransactionResponse;
import com.vovgoo.BankSystem.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Счёта", description = "Операции по счетам")
@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @Operation(summary = "Создать счёт", description = "Создаёт новый банковский счёт")
    @PostMapping
    public ResponseEntity<TransactionResponse> create(
            @RequestBody @Valid CreateAccountRequest createAccountRequest) {
        TransactionResponse resp = accountService.create(createAccountRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    @Operation(summary = "Удалить счёт", description = "Удаляет счёт по ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<TransactionResponse> delete(@PathVariable UUID id) {
        TransactionResponse resp = accountService.delete(id);
        return ResponseEntity.ok(resp);
    }

    @Operation(summary = "Пополнить счёт", description = "Внести средства на счёт")
    @PostMapping("/deposit")
    public ResponseEntity<TransactionResponse> deposit(@RequestBody @Valid DepositAccountRequest depositAccountRequest) {
        TransactionResponse resp = accountService.deposit(depositAccountRequest);
        return ResponseEntity.ok(resp);
    }

    @Operation(summary = "Снять средства", description = "Снять средства со счёта")
    @PostMapping("/withdraw")
    public ResponseEntity<TransactionResponse> withdraw(@RequestBody @Valid WithdrawAccountRequest withdrawAccountRequest) {
        TransactionResponse resp = accountService.withdraw(withdrawAccountRequest);
        return ResponseEntity.ok(resp);
    }

    @Operation(summary = "Перевести средства", description = "Перевести средства между счетами")
    @PostMapping("/transfer")
    public ResponseEntity<TransactionResponse> transfer(@RequestBody @Valid TransferAccountRequest transferAccountRequest) {
        TransactionResponse resp = accountService.transfer(transferAccountRequest);
        return ResponseEntity.ok(resp);
    }
}