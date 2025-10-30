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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<PageResponse<ClientResponse>> search(@Valid SearchClientRequest searchClientRequest) {
        PageResponse<ClientResponse> result = clientService.search(searchClientRequest);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Получить клиента", description = "Получить детали клиента по ID")
    @GetMapping("/{id}")
    public ResponseEntity<ClientDetailsResponse> get(@PathVariable UUID id) {
        ClientDetailsResponse result = clientService.get(id);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Создать клиента", description = "Создаёт нового клиента")
    @PostMapping
    public ResponseEntity<TransactionResponse> create(@RequestBody @Valid CreateClientRequest createClientRequest) {
        TransactionResponse resp = clientService.create(createClientRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    @Operation(summary = "Обновить клиента", description = "Обновляет данные клиента")
    @PutMapping
    public ResponseEntity<TransactionResponse> update(@RequestBody @Valid UpdateClientRequest updateClientRequest) {
        TransactionResponse resp = clientService.update(updateClientRequest);
        return ResponseEntity.ok(resp);
    }

    @Operation(summary = "Удалить клиента", description = "Удаляет клиента по ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<TransactionResponse> delete(@PathVariable UUID id) {
        TransactionResponse resp = clientService.delete(id);
        return ResponseEntity.ok(resp);
    }
}