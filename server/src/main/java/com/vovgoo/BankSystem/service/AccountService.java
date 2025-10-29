package com.vovgoo.BankSystem.service;

import com.vovgoo.BankSystem.dto.account.request.CreateAccountRequest;
import com.vovgoo.BankSystem.dto.account.request.DepositAccountRequest;
import com.vovgoo.BankSystem.dto.account.request.TransferAccountRequest;
import com.vovgoo.BankSystem.dto.account.request.WithdrawAccountRequest;
import com.vovgoo.BankSystem.dto.account.response.AccountResponse;
import com.vovgoo.BankSystem.dto.common.PageResponse;
import com.vovgoo.BankSystem.dto.transaction.TransactionResponse;
import org.springframework.data.domain.PageRequest;

import java.util.UUID;

public interface AccountService {
    PageResponse<AccountResponse> list(PageRequest pageRequest);
    AccountResponse get(UUID id);
    TransactionResponse create(CreateAccountRequest createAccountRequest);
    TransactionResponse delete(UUID id);
    TransactionResponse deposit(DepositAccountRequest depositAccountRequest);
    TransactionResponse withdraw(WithdrawAccountRequest withdrawAccountRequest);
    TransactionResponse transfer(TransferAccountRequest transferAccountRequest);
}