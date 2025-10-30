package com.vovgoo.BankSystem.dto.account.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record WithdrawAccountRequest(

        @NotNull(message = "ID счета обязателен")
        UUID accountId,

        @NotNull(message = "Сумма снятия обязательна")
        @DecimalMin(value = "0.01", message = "Сумма снятия должна быть больше 0")
        @Digits(integer = 17, fraction = 2, message = "Сумма должна иметь не более 17 цифр до запятой и ровно 2 после")
        BigDecimal amount

) {}