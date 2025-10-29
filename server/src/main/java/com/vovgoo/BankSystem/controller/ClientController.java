package com.vovgoo.BankSystem.controller;

import com.vovgoo.BankSystem.dto.client.request.CreateClientRequest;
import com.vovgoo.BankSystem.dto.client.request.SearchClientRequest;
import com.vovgoo.BankSystem.dto.client.request.UpdateClientRequest;
import com.vovgoo.BankSystem.dto.client.response.ClientResponse;
import com.vovgoo.BankSystem.dto.common.PageResponse;
import com.vovgoo.BankSystem.dto.transaction.TransactionResponse;
import com.vovgoo.BankSystem.service.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @GetMapping("/search")
    public PageResponse<ClientResponse> search(@Valid SearchClientRequest searchClientRequest) {
        return clientService.search(searchClientRequest);
    }

    @GetMapping("/{id}")
    public ClientResponse get(@PathVariable UUID id) {
        return clientService.get(id);
    }

    @PostMapping
    public TransactionResponse create(@RequestBody @Valid CreateClientRequest createClientRequest) {
        return clientService.create(createClientRequest);
    }

    @PutMapping
    public TransactionResponse update(@RequestBody @Valid UpdateClientRequest updateClientRequest) {
        return clientService.update(updateClientRequest);
    }

    @DeleteMapping("/{id}")
    public TransactionResponse delete(@PathVariable UUID id) {
        return clientService.delete(id);
    }
}