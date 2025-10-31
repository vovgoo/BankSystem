package com.vovgoo.BankSystem.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;

@ActiveProfiles("test")
@DisplayName("Account — проверка конструктора и операций с балансом")
class AccountTest {

    @Nested
    @DisplayName("Конструктор")
    class ConstructorTests {

        @Test
        @DisplayName("Устанавливает баланс в 0 и привязывает клиента")
        void constructor_shouldSetZeroBalanceAndClient() {
            Client client = new Client("Ivanov", "+375291234567");
            Account account = new Account(client);

            assertThat(account.getBalance()).isEqualByComparingTo(BigDecimal.ZERO);
            assertThat(account.getClient()).isEqualTo(client);
        }

        @Test
        @DisplayName("Бросает исключение, если клиент null")
        void constructor_shouldThrow_WhenClientNull() {
            assertThatThrownBy(() -> new Account(null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Клиент обязателен");
        }
    }

    @Nested
    @DisplayName("Пополнение счёта (deposit)")
    class DepositTests {

        @Test
        @DisplayName("Увеличивает баланс корректно")
        void deposit_shouldIncreaseBalance_correctly() {
            Account account = new Account(new Client("Ivanov", "+375291234567"));
            account.deposit(BigDecimal.valueOf(50.00));
            account.deposit(BigDecimal.valueOf(25.50));

            assertThat(account.getBalance()).isEqualByComparingTo(BigDecimal.valueOf(75.50));
            assertThat(account.getBalance().scale()).isEqualTo(2);
        }

        @Test
        @DisplayName("Бросает исключение при недопустимой сумме")
        void deposit_shouldThrow_WhenInvalidAmount() {
            Account account = new Account(new Client("Ivanov", "+375291234567"));
            assertThatThrownBy(() -> account.deposit(null)).isInstanceOf(IllegalArgumentException.class);
            assertThatThrownBy(() -> account.deposit(BigDecimal.ZERO)).isInstanceOf(IllegalArgumentException.class);
            assertThatThrownBy(() -> account.deposit(BigDecimal.valueOf(-1))).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("Бросает исключение, если сумма имеет больше 2 знаков после запятой")
        void deposit_shouldThrow_WhenTooManyDecimalPlaces() {
            Account account = new Account(new Client("Ivanov", "+375291234567"));
            assertThatThrownBy(() -> account.deposit(new BigDecimal("10.123")))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("не может иметь больше 2 знаков после запятой");
        }
    }

    @Nested
    @DisplayName("Снятие со счёта (withdraw)")
    class WithdrawTests {

        @Test
        @DisplayName("Уменьшает баланс корректно")
        void withdraw_shouldDecreaseBalance_correctly() {
            Account account = new Account(new Client("Ivanov", "+375291234567"));
            account.deposit(BigDecimal.valueOf(100.00));
            account.withdraw(BigDecimal.valueOf(40.25));

            assertThat(account.getBalance()).isEqualByComparingTo(BigDecimal.valueOf(59.75));
            assertThat(account.getBalance().scale()).isEqualTo(2);
        }

        @Test
        @DisplayName("Бросает исключение при недопустимой сумме")
        void withdraw_shouldThrow_WhenInvalidAmount() {
            Account account = new Account(new Client("Ivanov", "+375291234567"));
            account.deposit(BigDecimal.valueOf(50));

            assertThatThrownBy(() -> account.withdraw(null)).isInstanceOf(IllegalArgumentException.class);
            assertThatThrownBy(() -> account.withdraw(BigDecimal.ZERO)).isInstanceOf(IllegalArgumentException.class);
            assertThatThrownBy(() -> account.withdraw(BigDecimal.valueOf(-10))).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("Бросает исключение, если сумма имеет больше 2 знаков после запятой")
        void withdraw_shouldThrow_WhenTooManyDecimalPlaces() {
            Account account = new Account(new Client("Ivanov", "+375291234567"));
            account.deposit(BigDecimal.valueOf(50));
            assertThatThrownBy(() -> account.withdraw(new BigDecimal("10.123")))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("не может иметь больше 2 знаков после запятой");
        }

        @Test
        @DisplayName("Бросает исключение, если снимаемая сумма превышает баланс")
        void withdraw_shouldThrow_WhenExceedsBalance() {
            Account account = new Account(new Client("Ivanov", "+375291234567"));
            account.deposit(BigDecimal.valueOf(30));
            assertThatThrownBy(() -> account.withdraw(BigDecimal.valueOf(50)))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("Недостаточно средств");
        }
    }

    @Nested
    @DisplayName("Баланс после множества операций")
    class BalanceScaleTests {

        @Test
        @DisplayName("Баланс всегда имеет scale 2")
        void balance_shouldAlwaysHaveScaleTwo_afterMultipleOperations() {
            Account account = new Account(new Client("Ivanov", "+375291234567"));
            account.deposit(BigDecimal.valueOf(10.10));
            account.deposit(BigDecimal.valueOf(0.10));
            account.withdraw(BigDecimal.valueOf(5.05));

            assertThat(account.getBalance().scale()).isEqualTo(2);
            assertThat(account.getBalance()).isEqualByComparingTo(new BigDecimal("5.15"));
        }

        @Test
        @DisplayName("Комбинированный стресс-тест депозита и снятия")
        void depositAndWithdraw_combinedStressTest() {
            Account account = new Account(new Client("Ivanov", "+375291234567"));
            for (int i = 0; i < 100; i++) account.deposit(BigDecimal.valueOf(1.23));
            assertThat(account.getBalance()).isEqualByComparingTo(new BigDecimal("123.00"));

            for (int i = 0; i < 50; i++) account.withdraw(BigDecimal.valueOf(0.50));
            assertThat(account.getBalance()).isEqualByComparingTo(new BigDecimal("98.00"));
        }
    }
}