package com.vovgoo.BankSystem.service.impl;

import com.vovgoo.BankSystem.dto.client.request.CreateClientRequest;
import com.vovgoo.BankSystem.dto.client.request.SearchClientRequest;
import com.vovgoo.BankSystem.dto.client.request.UpdateClientRequest;
import com.vovgoo.BankSystem.dto.client.response.ClientDetailsResponse;
import com.vovgoo.BankSystem.dto.client.response.ClientResponse;
import com.vovgoo.BankSystem.dto.common.PageParams;
import com.vovgoo.BankSystem.dto.common.PageResponse;
import com.vovgoo.BankSystem.dto.transaction.TransactionResponse;
import com.vovgoo.BankSystem.entity.Account;
import com.vovgoo.BankSystem.entity.Client;
import com.vovgoo.BankSystem.exception.client.ClientAlreadyExistsException;
import com.vovgoo.BankSystem.exception.client.ClientHasActiveAccountsException;
import com.vovgoo.BankSystem.mapper.ClientMapper;
import com.vovgoo.BankSystem.repository.AccountRepository;
import com.vovgoo.BankSystem.repository.ClientRepository;
import com.vovgoo.BankSystem.service.ClientService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final AccountRepository accountRepository;
    private final ClientMapper clientMapper;

    @Override
    public PageResponse<ClientResponse> search(SearchClientRequest searchClientRequest) {
        String lastName = searchClientRequest.getLastName();
        PageParams pageParams = searchClientRequest.getPageParams();

        Pageable pageable = PageRequest.of(pageParams.getPage(), pageParams.getSize());

        Page<Client> clientsPage;

        if (lastName == null || lastName.isBlank()) {
            clientsPage = clientRepository.findAll(pageable);
        } else {
            clientsPage = clientRepository.findByLastNameContainingIgnoreCase(lastName, pageable);
        }

        List<ClientResponse> content = clientsPage.stream()
                .map(client -> clientMapper.to(client, client.getAccounts().stream()
                        .map(Account::getBalance)
                        .reduce(BigDecimal.ZERO, BigDecimal::add)))
                .toList();

        return PageResponse.of(content, clientsPage.getNumber(), clientsPage.getSize(), clientsPage.getTotalElements());
    }

    @Override
    public ClientDetailsResponse get(UUID id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Клиент не найден"));

        return clientMapper.to(client);
    }

    @Override
    public TransactionResponse create(CreateClientRequest createClientRequest) {
        if (clientRepository.existsByPhone(createClientRequest.getPhone())) {
            throw new ClientAlreadyExistsException(createClientRequest.getPhone());
        }

        Client client = new Client(createClientRequest.getLastName(), createClientRequest.getPhone());

        clientRepository.save(client);

        return TransactionResponse.approve("Клиент был успешно создан");
    }

    @Override
    public TransactionResponse update(UpdateClientRequest updateClientRequest) {
        Client client = clientRepository.findById(updateClientRequest.getId())
                .orElseThrow(() -> new EntityNotFoundException("Клиент не найден"));

        String newPhone = updateClientRequest.getPhone();

        if (!client.getPhone().equals(newPhone) && clientRepository.existsByPhone(newPhone)) {
            throw new ClientAlreadyExistsException(newPhone);
        }

        client.updateLastName(updateClientRequest.getLastName());
        client.updatePhone(newPhone);

        clientRepository.save(client);
        return TransactionResponse.approve("Данные клиента успешно обновлены");
    }

    @Override
    public TransactionResponse delete(UUID id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Клиент не найден"));

        long accountCount = accountRepository.countByClientId(id);
        if (accountCount > 0) {
            throw new ClientHasActiveAccountsException(accountCount);
        }

        clientRepository.delete(client);
        return TransactionResponse.approve("Клиент был успешно удалён");
    }
}