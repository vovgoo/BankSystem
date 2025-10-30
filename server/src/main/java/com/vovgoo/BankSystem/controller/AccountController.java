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
    public TransactionResponse create(@RequestBody @Valid CreateAccountRequest createAccountRequest) {
        return accountService.create(createAccountRequest);
    }

    @Operation(summary = "Удалить счёт", description = "Удаляет счёт по ID")
    @DeleteMapping("/{id}")
    public TransactionResponse delete(@PathVariable UUID id) {
        return accountService.delete(id);
    }

    @Operation(summary = "Пополнить счёт", description = "Внести средства на счёт")
    @PostMapping("/deposit")
    public TransactionResponse deposit(@RequestBody @Valid DepositAccountRequest depositAccountRequest) {
        return accountService.deposit(depositAccountRequest);
    }

    @Operation(summary = "Снять средства", description = "Снять средства со счёта")
    @PostMapping("/withdraw")
    public TransactionResponse withdraw(@RequestBody @Valid WithdrawAccountRequest withdrawAccountRequest) {
        return accountService.withdraw(withdrawAccountRequest);
    }

    @Operation(summary = "Перевести средства", description = "Перевести средства между счетами")
    @PostMapping("/transfer")
    public TransactionResponse transfer(@RequestBody @Valid TransferAccountRequest transferAccountRequest) {
        return accountService.transfer(transferAccountRequest);
    }
}