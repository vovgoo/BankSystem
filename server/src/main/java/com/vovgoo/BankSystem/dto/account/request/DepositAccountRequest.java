package com.vovgoo.BankSystem.dto.account.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Builder
public class DepositAccountRequest {

    @NotNull(message = "ID счета обязателен")
    private final UUID accountId;

    @NotNull(message = "Сумма пополнения обязательна")
    @DecimalMin(value = "0.01", message = "Сумма пополнения должна быть больше 0")
    @Digits(integer = 17, fraction = 2, message = "Сумма должна иметь не более 17 цифр до запятой и ровно 2 после")
    private final BigDecimal amount;
}