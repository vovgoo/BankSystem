package com.vovgoo.BankSystem.service.impl;

import com.vovgoo.BankSystem.config.properties.TransactionProperties;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final ClientRepository clientRepository;
    private final TransactionProperties transactionProperties;

    @Override
    @Transactional
    public TransactionResponse create(CreateAccountRequest createAccountRequest) {
        log.info("Создание счёта: clientId={}", createAccountRequest.clientId());

        Client client = clientRepository.findById(createAccountRequest.clientId())
                .orElseThrow(() -> new EntityNotFoundException("Клиент не найден"));

        Account account = new Account(client);
        accountRepository.save(account);

        log.info("Счёт успешно создан: accountId={}, clientId={}", account.getId(), client.getId());
        return TransactionResponse.approve("Счёт успешно создан");
    }

    @Override
    @Transactional
    public TransactionResponse delete(UUID id) {
        log.info("Удаление счёта: accountId={}", id);

        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Счёт не найден"));

        if (account.getBalance().compareTo(BigDecimal.ZERO) > 0) {
            log.warn("Невозможно удалить счёт с ненулевым балансом: accountId={}, balance={}", id, account.getBalance());
            throw new AccountBalanceNotZeroException();
        }

        accountRepository.delete(account);
        log.info("Счёт успешно удалён: accountId={}", id);
        return TransactionResponse.approve("Счёт успешно удалён");
    }

    @Override
    @Transactional
    public TransactionResponse deposit(DepositAccountRequest depositAccountRequest) {
        log.info("Пополнение счёта: accountId={}, amount={}", depositAccountRequest.accountId(), depositAccountRequest.amount());

        Account account = accountRepository.findById(depositAccountRequest.accountId())
                .orElseThrow(() -> new EntityNotFoundException("Счёт не найден"));

        account.deposit(depositAccountRequest.amount());
        accountRepository.save(account);

        log.info("Счёт успешно пополнен: accountId={}, newBalance={}", account.getId(), account.getBalance());
        return TransactionResponse.approve("Счёт успешно пополнен на сумму " + depositAccountRequest.amount());
    }

    @Override
    @Transactional
    public TransactionResponse withdraw(WithdrawAccountRequest withdrawAccountRequest) {
        log.info("Снятие со счёта: accountId={}, amount={}", withdrawAccountRequest.accountId(), withdrawAccountRequest.amount());

        Account account = accountRepository.findById(withdrawAccountRequest.accountId())
                .orElseThrow(() -> new EntityNotFoundException("Счёт не найден"));

        if (account.getBalance().compareTo(withdrawAccountRequest.amount()) < 0) {
            log.warn("Недостаточно средств: accountId={}, balance={}, requested={}",
                    account.getId(), account.getBalance(), withdrawAccountRequest.amount());
            throw new InsufficientFundsException();
        }

        account.withdraw(withdrawAccountRequest.amount());
        accountRepository.save(account);

        log.info("Средства успешно сняты: accountId={}, newBalance={}", account.getId(), account.getBalance());
        return TransactionResponse.approve("Со счёта успешно снято " + withdrawAccountRequest.amount());
    }

    @Override
    @Transactional
    public TransactionResponse transfer(TransferAccountRequest transferAccountRequest) {
        log.info("Перевод средств: fromAccountId={}, toAccountId={}, amount={}",
                transferAccountRequest.fromAccountId(), transferAccountRequest.toAccountId(), transferAccountRequest.amount());

        if (transferAccountRequest.fromAccountId().equals(transferAccountRequest.toAccountId())) {
            log.warn("Попытка перевода на тот же счёт: accountId={}", transferAccountRequest.fromAccountId());
            throw new SameAccountTransferException();
        }

        Account fromAccount = accountRepository.findById(transferAccountRequest.fromAccountId())
                .orElseThrow(() -> new EntityNotFoundException("Счёт отправителя не найден"));

        Account toAccount = accountRepository.findById(transferAccountRequest.toAccountId())
                .orElseThrow(() -> new EntityNotFoundException("Счёт получателя не найден"));

        BigDecimal amount = transferAccountRequest.amount();
        BigDecimal commission = amount.multiply(BigDecimal.valueOf(transactionProperties.getTransferCommissionPercent()))
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        BigDecimal total = amount.add(commission);

        if (fromAccount.getBalance().compareTo(total) < 0) {
            log.warn("Недостаточно средств на счёте отправителя: accountId={}, balance={}, required={}",
                    fromAccount.getId(), fromAccount.getBalance(), total);
            throw new InsufficientFundsException();
        }

        fromAccount.withdraw(total);
        toAccount.deposit(amount);

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        log.info("Перевод выполнен: fromAccountId={}, toAccountId={}, amount={}, commission={}",
                fromAccount.getId(), toAccount.getId(), amount, commission);

        return TransactionResponse.approve(
                String.format("Перевод %.2f выполнен успешно. Комиссия составила %.2f", amount, commission)
        );
    }
}