package com.vovgoo.BankSystem.dto.transaction;

import com.vovgoo.BankSystem.dto.transaction.enums.TransactionStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class TransactionResponse {

    private final TransactionStatus status;

    private final String message;

    @Builder.Default
    private final LocalDateTime timestamp = LocalDateTime.now();
}