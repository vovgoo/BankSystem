package com.vovgoo.BankSystem.controller;

import com.vovgoo.BankSystem.dto.client.request.CreateClientRequest;
import com.vovgoo.BankSystem.dto.client.request.SearchClientRequest;
import com.vovgoo.BankSystem.dto.client.request.UpdateClientRequest;
import com.vovgoo.BankSystem.dto.client.response.ClientDetailsResponse;
import com.vovgoo.BankSystem.dto.client.response.ClientResponse;
import com.vovgoo.BankSystem.dto.common.PageResponse;
import com.vovgoo.BankSystem.dto.transaction.TransactionResponse;
import com.vovgoo.BankSystem.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Клиенты", description = "Операции по клиентам")
@RestController
@RequestMapping("/api/v1/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @Operation(summary = "Поиск клиентов", description = "Поиск клиентов по параметрам")
    @GetMapping("/search")
    public PageResponse<ClientResponse> search(@Valid SearchClientRequest searchClientRequest) {
        return clientService.search(searchClientRequest);
    }

    @Operation(summary = "Получить клиента", description = "Получить детали клиента по ID")
    @GetMapping("/{id}")
    public ClientDetailsResponse get(@PathVariable UUID id) {
        return clientService.get(id);
    }

    @Operation(summary = "Создать клиента", description = "Создаёт нового клиента")
    @PostMapping
    public TransactionResponse create(@RequestBody @Valid CreateClientRequest createClientRequest) {
        return clientService.create(createClientRequest);
    }

    @Operation(summary = "Обновить клиента", description = "Обновляет данные клиента")
    @PutMapping
    public TransactionResponse update(@RequestBody @Valid UpdateClientRequest updateClientRequest) {
        return clientService.update(updateClientRequest);
    }

    @Operation(summary = "Удалить клиента", description = "Удаляет клиента по ID")
    @DeleteMapping("/{id}")
    public TransactionResponse delete(@PathVariable UUID id) {
        return clientService.delete(id);
    }
}