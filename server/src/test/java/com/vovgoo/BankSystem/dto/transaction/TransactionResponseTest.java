package com.vovgoo.BankSystem.dto.transaction;

import com.vovgoo.BankSystem.dto.transaction.enums.TransactionStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

class TransactionResponseTest {

    @Test
    void approve_shouldSetCorrectStatusAndMessage() {
        String message = "Payment successful";
        TransactionResponse response = TransactionResponse.approve(message);

        assertThat(response.getStatus()).isEqualTo(TransactionStatus.APPROVED);
        assertThat(response.getMessage()).isEqualTo(message);
        assertThat(response.getTimestamp()).isNotNull();
        assertThat(response.getTimestamp()).isBeforeOrEqualTo(LocalDateTime.now());
    }

    @Test
    void decline_shouldSetCorrectStatusAndMessage() {
        String message = "Insufficient funds";
        TransactionResponse response = TransactionResponse.decline(message);

        assertThat(response.getStatus()).isEqualTo(TransactionStatus.DECLINED);
        assertThat(response.getMessage()).isEqualTo(message);
        assertThat(response.getTimestamp()).isNotNull();
        assertThat(response.getTimestamp()).isBeforeOrEqualTo(LocalDateTime.now());
    }
}