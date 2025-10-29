package com.vovgoo.BankSystem.dto.client.request;

import com.vovgoo.BankSystem.dto.common.PageRequest;
import jakarta.validation.Valid;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SearchClientRequest {

    private final String lastName;

    @Valid
    private final PageRequest pageRequest;
}