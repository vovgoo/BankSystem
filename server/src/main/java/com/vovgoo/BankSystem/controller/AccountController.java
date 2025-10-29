package com.vovgoo.BankSystem.controller;

import com.vovgoo.BankSystem.dto.account.request.CreateAccountRequest;
import com.vovgoo.BankSystem.dto.account.request.DepositAccountRequest;
import com.vovgoo.BankSystem.dto.account.request.TransferAccountRequest;
import com.vovgoo.BankSystem.dto.account.request.WithdrawAccountRequest;
import com.vovgoo.BankSystem.dto.account.response.AccountResponse;
import com.vovgoo.BankSystem.dto.common.PageResponse;
import com.vovgoo.BankSystem.dto.transaction.TransactionResponse;
import com.vovgoo.BankSystem.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping
    public PageResponse<AccountResponse> list(@Valid PageRequest pageRequest) {
        return accountService.list(pageRequest);
    }

    @GetMapping("/{id}")
    public AccountResponse get(@PathVariable UUID id) {
        return accountService.get(id);
    }

    @PostMapping
    public TransactionResponse create(@RequestBody @Valid CreateAccountRequest createAccountRequest) {
        return accountService.create(createAccountRequest);
    }

    @DeleteMapping("/{id}")
    public TransactionResponse delete(@PathVariable UUID id) {
        return accountService.delete(id);
    }

    @PostMapping("/deposit")
    public TransactionResponse deposit(@RequestBody @Valid DepositAccountRequest depositAccountRequest) {
        return accountService.deposit(depositAccountRequest);
    }

    @PostMapping("/withdraw")
    public TransactionResponse withdraw(@RequestBody @Valid WithdrawAccountRequest withdrawAccountRequest) {
        return accountService.withdraw(withdrawAccountRequest);
    }

    @PostMapping("/transfer")
    public TransactionResponse transfer(@RequestBody TransferAccountRequest transferAccountRequest) {
        return accountService.transfer(transferAccountRequest);
    }
}