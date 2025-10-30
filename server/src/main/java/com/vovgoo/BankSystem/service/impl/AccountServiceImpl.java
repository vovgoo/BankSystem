package com.vovgoo.BankSystem.service.impl;

import com.vovgoo.BankSystem.dto.account.request.CreateAccountRequest;
import com.vovgoo.BankSystem.dto.account.request.DepositAccountRequest;
import com.vovgoo.BankSystem.dto.account.request.TransferAccountRequest;
import com.vovgoo.BankSystem.dto.account.request.WithdrawAccountRequest;
import com.vovgoo.BankSystem.dto.transaction.TransactionResponse;
import com.vovgoo.BankSystem.entity.Account;
import com.vovgoo.BankSystem.entity.Client;
import com.vovgoo.BankSystem.exception.account.AccountBalanceNotZeroException;
import com.vovgoo.BankSystem.exception.account.InsufficientFundsException;
import com.vovgoo.BankSystem.exception.account.SameAccountTransferException;
import com.vovgoo.BankSystem.repository.AccountRepository;
import com.vovgoo.BankSystem.repository.ClientRepository;
import com.vovgoo.BankSystem.service.AccountService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final ClientRepository clientRepository;

    @Override
    @Transactional
    public TransactionResponse create(CreateAccountRequest createAccountRequest) {
        UUID clientId = createAccountRequest.getClientId();

        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new EntityNotFoundException("Клиент не найден"));

        Account account = new Account(client);

        accountRepository.save(account);

        return TransactionResponse.approve("Счёт успешно создан");
    }

    @Override
    @Transactional
    public TransactionResponse delete(UUID id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Счёт не найден"));

        if (account.getBalance().compareTo(BigDecimal.ZERO) > 0) {
            throw new AccountBalanceNotZeroException();
        }

        accountRepository.delete(account);

        return TransactionResponse.approve("Счёт успешно удалён");
    }

    @Override
    @Transactional
    public TransactionResponse deposit(DepositAccountRequest depositAccountRequest) {
        Account account = accountRepository.findById(depositAccountRequest.getAccountId())
                .orElseThrow(() -> new EntityNotFoundException("Счёт не найден"));

        account.deposit(depositAccountRequest.getAmount());

        accountRepository.save(account);

        return TransactionResponse.approve("Счёт успешно пополнен на сумму " + depositAccountRequest.getAmount());
    }

    @Override
    @Transactional
    public TransactionResponse withdraw(WithdrawAccountRequest withdrawAccountRequest) {
        Account account = accountRepository.findById(withdrawAccountRequest.getAccountId())
                .orElseThrow(() -> new EntityNotFoundException("Счёт не найден"));

        if (account.getBalance().compareTo(withdrawAccountRequest.getAmount()) < 0) {
            throw new InsufficientFundsException();
        }

        account.withdraw(withdrawAccountRequest.getAmount());

        accountRepository.save(account);

        return TransactionResponse.approve("Со счёта успешно снято " + withdrawAccountRequest.getAmount());
    }

    @Override
    @Transactional
    public TransactionResponse transfer(TransferAccountRequest transferAccountRequest) {
        if (transferAccountRequest.getFromAccountId().equals(transferAccountRequest.getToAccountId())) {
            throw new SameAccountTransferException();
        }

        Account fromAccount = accountRepository.findById(transferAccountRequest.getFromAccountId())
                .orElseThrow(() -> new EntityNotFoundException("Счёт отправителя не найден"));

        Account toAccount = accountRepository.findById(transferAccountRequest.getToAccountId())
                .orElseThrow(() -> new EntityNotFoundException("Счёт получателя не найден"));

        BigDecimal amount = transferAccountRequest.getAmount();
        BigDecimal commission = amount.multiply(new BigDecimal("0.05"));
        BigDecimal total = amount.add(commission);

        if (fromAccount.getBalance().compareTo(total) < 0) {
            throw new InsufficientFundsException();
        }

        fromAccount.withdraw(total);
        toAccount.deposit(amount);

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        return TransactionResponse.approve(
                String.format("Перевод %.2f выполнен успешно. Комиссия составила %.2f", amount, commission)
        );
    }
}