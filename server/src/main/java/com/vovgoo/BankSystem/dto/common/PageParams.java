package com.vovgoo.BankSystem.dto.common;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PageParams {

    @NotNull(message = "Номер страницы обязателен")
    @Min(value = 0, message = "Номер страницы не может быть отрицательным")
    private final Integer page;

    @NotNull(message = "Размер страницы обязателен")
    @Min(value = 0, message = "Размер страницы не может быть отрицательным")
    @Max(value = 100, message = "Размер страницы не может быть больше 100")
    private final Integer size;
}