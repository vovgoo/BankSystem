package com.vovgoo.BankSystem.dto.transaction;

import com.vovgoo.BankSystem.dto.transaction.enums.TransactionStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TransactionResponse {

    private final TransactionStatus status;
    private final String message;
    private final LocalDateTime timestamp;

    public static TransactionResponse approve(String message) {
        return new TransactionResponse(TransactionStatus.APPROVED, message, LocalDateTime.now());
    }

    public static TransactionResponse decline(String message) {
        return new TransactionResponse(TransactionStatus.DECLINED, message, LocalDateTime.now());
    }
}