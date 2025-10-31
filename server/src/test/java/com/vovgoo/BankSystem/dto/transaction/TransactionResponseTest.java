package com.vovgoo.BankSystem.dto.transaction;

import com.vovgoo.BankSystem.dto.transaction.enums.TransactionStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("TransactionResponse — фабричные методы approve/decline")
class TransactionResponseTest {

    @Nested
    @DisplayName("Метод approve()")
    class ApproveMethodTests {

        @Test
        @DisplayName("Должен корректно установить статус APPROVED, сообщение и время")
        void approve_shouldSetCorrectStatusAndMessage() {
            String message = "Payment successful";

            TransactionResponse response = TransactionResponse.approve(message);

            assertThat(response.getStatus()).isEqualTo(TransactionStatus.APPROVED);
            assertThat(response.getMessage()).isEqualTo(message);
            assertThat(response.getTimestamp())
                    .isNotNull()
                    .isBeforeOrEqualTo(LocalDateTime.now());
        }
    }

    @Nested
    @DisplayName("Метод decline()")
    class DeclineMethodTests {

        @Test
        @DisplayName("Должен корректно установить статус DECLINED, сообщение и время")
        void decline_shouldSetCorrectStatusAndMessage() {
            String message = "Insufficient funds";

            TransactionResponse response = TransactionResponse.decline(message);

            assertThat(response.getStatus()).isEqualTo(TransactionStatus.DECLINED);
            assertThat(response.getMessage()).isEqualTo(message);
            assertThat(response.getTimestamp())
                    .isNotNull()
                    .isBeforeOrEqualTo(LocalDateTime.now());
        }
    }
}
