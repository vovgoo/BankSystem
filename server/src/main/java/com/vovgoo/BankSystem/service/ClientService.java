package com.vovgoo.BankSystem.service;

import com.vovgoo.BankSystem.dto.client.request.CreateClientRequest;
import com.vovgoo.BankSystem.dto.client.request.SearchClientRequest;
import com.vovgoo.BankSystem.dto.client.request.UpdateClientRequest;
import com.vovgoo.BankSystem.dto.client.response.ClientDetailsResponse;
import com.vovgoo.BankSystem.dto.client.response.ClientResponse;
import com.vovgoo.BankSystem.dto.common.PageParams;
import com.vovgoo.BankSystem.dto.common.PageResponse;
import com.vovgoo.BankSystem.dto.transaction.TransactionResponse;

import java.util.UUID;

public interface ClientService {
    PageResponse<ClientResponse> search(SearchClientRequest searchClientRequest, PageParams pageParams);
    ClientDetailsResponse get(UUID id, PageParams pageParams);
    TransactionResponse create(CreateClientRequest createClientRequest);
    TransactionResponse update(UpdateClientRequest updateClientRequest);
    TransactionResponse delete(UUID id);
}