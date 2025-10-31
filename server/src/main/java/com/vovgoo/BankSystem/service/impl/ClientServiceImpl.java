package com.vovgoo.BankSystem.service.impl;

import com.vovgoo.BankSystem.dto.client.request.CreateClientRequest;
import com.vovgoo.BankSystem.dto.client.request.SearchClientRequest;
import com.vovgoo.BankSystem.dto.client.request.UpdateClientRequest;
import com.vovgoo.BankSystem.dto.client.response.ClientDetailsResponse;
import com.vovgoo.BankSystem.dto.client.response.ClientResponse;
import com.vovgoo.BankSystem.dto.common.PageParams;
import com.vovgoo.BankSystem.dto.common.PageResponse;
import com.vovgoo.BankSystem.dto.transaction.TransactionResponse;
import com.vovgoo.BankSystem.entity.Client;
import com.vovgoo.BankSystem.exception.client.ClientAlreadyExistsException;
import com.vovgoo.BankSystem.exception.client.ClientHasActiveAccountsException;
import com.vovgoo.BankSystem.mapper.ClientMapper;
import com.vovgoo.BankSystem.repository.AccountRepository;
import com.vovgoo.BankSystem.repository.ClientRepository;
import com.vovgoo.BankSystem.service.ClientService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final AccountRepository accountRepository;
    private final ClientMapper clientMapper;

    @Override
    public PageResponse<ClientResponse> search(SearchClientRequest searchClientRequest, PageParams pageParams) {
        log.info("Поиск клиентов: lastName={}, page={}, size={}",
                searchClientRequest.lastName(), pageParams.page(), pageParams.size());

        Pageable pageable = PageRequest.of(pageParams.page(), pageParams.size());
        Page<ClientResponse> clientsPage = clientRepository.findClientSummaries(searchClientRequest.lastName(), pageable);

        log.info("Найдено {} клиентов для lastName={}", clientsPage.getTotalElements(), searchClientRequest.lastName());
        return PageResponse.of(clientsPage);
    }

    @Override
    public ClientDetailsResponse get(UUID id) {
        log.info("Получение клиента по ID={}", id);
        Client client = clientRepository.findByIdWithAccounts(id)
                .orElseThrow(() -> new EntityNotFoundException("Клиент не найден"));

        log.info("Клиент успешно найден: id={}", id);
        return clientMapper.toClientDetailsResponse(client);
    }

    @Override
    @Transactional
    public TransactionResponse create(CreateClientRequest createClientRequest) {
        log.info("Создание клиента: lastName={}, phone={}", createClientRequest.lastName(), createClientRequest.phone());

        if (clientRepository.existsByPhone(createClientRequest.phone())) {
            log.warn("Клиент с телефоном {} уже существует", createClientRequest.phone());
            throw new ClientAlreadyExistsException(createClientRequest.phone());
        }

        Client client = new Client(createClientRequest.lastName(), createClientRequest.phone());

        clientRepository.save(client);

        log.info("Клиент успешно создан: id={}", client.getId());
        return TransactionResponse.approve("Клиент был успешно создан");
    }

    @Override
    @Transactional
    public TransactionResponse update(UpdateClientRequest updateClientRequest) {
        log.info("Обновление клиента: id={}, lastName={}, phone={}",
                updateClientRequest.id(), updateClientRequest.lastName(), updateClientRequest.phone());

        Client client = clientRepository.findById(updateClientRequest.id())
                .orElseThrow(() -> new EntityNotFoundException("Клиент не найден"));

        String newPhone = updateClientRequest.phone();

        if (!client.getPhone().equals(newPhone) && clientRepository.existsByPhone(newPhone)) {
            log.warn("Телефон {} уже используется другим клиентом", newPhone);
            throw new ClientAlreadyExistsException(newPhone);
        }

        client.setLastName(updateClientRequest.lastName());
        client.setPhone(newPhone);

        clientRepository.save(client);

        log.info("Клиент успешно обновлён: id={}", updateClientRequest.id());
        return TransactionResponse.approve("Данные клиента успешно обновлены");
    }

    @Override
    @Transactional
    public TransactionResponse delete(UUID id) {
        log.info("Удаление клиента: id={}", id);
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Клиент не найден"));

        long accountCount = accountRepository.countByClientId(id);
        if (accountCount > 0) {
            log.warn("Клиент имеет {} активных счетов, удаление невозможно", accountCount);
            throw new ClientHasActiveAccountsException(accountCount);
        }

        clientRepository.delete(client);

        log.info("Клиент успешно удалён: id={}", id);
        return TransactionResponse.approve("Клиент был успешно удалён");
    }
}