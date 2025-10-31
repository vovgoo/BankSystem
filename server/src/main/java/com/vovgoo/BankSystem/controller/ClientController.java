package com.vovgoo.BankSystem.controller;

import com.vovgoo.BankSystem.dto.account.request.CreateAccountRequest;
import com.vovgoo.BankSystem.dto.account.request.DepositAccountRequest;
import com.vovgoo.BankSystem.dto.account.request.TransferAccountRequest;
import com.vovgoo.BankSystem.dto.account.request.WithdrawAccountRequest;
import com.vovgoo.BankSystem.dto.client.request.CreateClientRequest;
import com.vovgoo.BankSystem.dto.client.request.SearchClientRequest;
import com.vovgoo.BankSystem.dto.client.request.UpdateClientRequest;
import com.vovgoo.BankSystem.dto.client.response.ClientDetailsResponse;
import com.vovgoo.BankSystem.dto.client.response.ClientResponse;
import com.vovgoo.BankSystem.dto.common.PageParams;
import com.vovgoo.BankSystem.dto.common.PageResponse;
import com.vovgoo.BankSystem.dto.transaction.TransactionResponse;
import com.vovgoo.BankSystem.service.AccountService;
import com.vovgoo.BankSystem.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    @Operation(
            summary = "Поиск клиентов",
            description = "Поиск клиентов по фамилии с постраничным выводом",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Результаты поиска клиентов",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = PageResponse.class),
                                    examples = @ExampleObject(
                                            value = "{ \"content\": [ { \"id\": \"123e4567-e89b-12d3-a456-426614174000\", \"lastName\": \"Иванов\", \"phone\": \"+375291234567\", \"totalBalance\": 1500.00 } ], \"page\": 0, \"size\": 10, \"totalElements\": 1, \"totalPages\": 1 }"
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "Некорректные параметры запроса",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TransactionResponse.class),
                                    examples = @ExampleObject(value = "{ \"status\": \"DECLINED\", \"message\": \"Некорректные параметры запроса\", \"timestamp\": \"2025-11-01T12:00:00\" }")
                            )
                    ),
                    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервиса",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TransactionResponse.class),
                                    examples = @ExampleObject(value = "{ \"status\": \"DECLINED\", \"message\": \"Внутренняя ошибка сервиса\", \"timestamp\": \"2025-11-01T12:00:01\" }")
                            )
                    )
            }
    )
    @GetMapping("/search")
    public ResponseEntity<PageResponse<ClientResponse>> search(@Valid SearchClientRequest searchClientRequest,
                                                               @Valid PageParams pageParams) {
        return ResponseEntity.ok(clientService.search(searchClientRequest, pageParams));
    }

    @Operation(
            summary = "Получить клиента",
            description = "Возвращает детальную информацию о клиенте по ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Детали клиента",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ClientDetailsResponse.class),
                                    examples = @ExampleObject(
                                            value = "{ \"id\": \"123e4567-e89b-12d3-a456-426614174000\", \"lastName\": \"Иванов\", \"phone\": \"+375291234567\", \"accounts\": [ { \"id\": \"123e4567-e89b-12d3-a456-426614174001\", \"balance\": 1500.00 } ] }"
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Клиент не найден",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TransactionResponse.class),
                                    examples = @ExampleObject(
                                            value = "{ \"status\": \"DECLINED\", \"message\": \"Клиент не найден\", \"timestamp\": \"2025-11-01T12:01:00\" }"
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервиса",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TransactionResponse.class),
                                    examples = @ExampleObject(
                                            value = "{ \"status\": \"DECLINED\", \"message\": \"Внутренняя ошибка сервиса\", \"timestamp\": \"2025-11-01T12:01:01\" }"
                                    )
                            )
                    )
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<ClientDetailsResponse> get(@PathVariable UUID id) {
        return ResponseEntity.ok(clientService.get(id));
    }

    @Operation(
            summary = "Создать клиента",
            description = "Создаёт нового клиента",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CreateClientRequest.class),
                            examples = @ExampleObject(value = "{ \"lastName\": \"Иванов\", \"phone\": \"+375291234567\" }")
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Клиент успешно создан",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TransactionResponse.class),
                                    examples = @ExampleObject(value = "{ \"status\": \"APPROVED\", \"message\": \"Клиент был успешно создан\", \"timestamp\": \"2025-11-01T12:41:00\" }")
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "Некорректные данные запроса",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TransactionResponse.class),
                                    examples = @ExampleObject(value = "{ \"status\": \"DECLINED\", \"message\": \"lastName: Фамилия не должна быть пустой; phone: Телефон обязателен;\", \"timestamp\": \"2025-11-01T12:41:00\" }")
                            )
                    ),
                    @ApiResponse(responseCode = "409", description = "Клиент с таким телефоном уже существует",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TransactionResponse.class),
                                    examples = @ExampleObject(value = "{ \"status\": \"DECLINED\", \"message\": \"Клиент с телефоном +375291234567 уже существует\", \"timestamp\": \"2025-11-01T12:41:00\" }")
                            )
                    ),
                    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервиса",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TransactionResponse.class),
                                    examples = @ExampleObject(value = "{ \"status\": \"DECLINED\", \"message\": \"Внутренняя ошибка сервиса\", \"timestamp\": \"2025-11-01T12:41:00\" }")
                            )
                    )
            }
    )
    @PostMapping
    public ResponseEntity<TransactionResponse> create(@RequestBody @Valid CreateClientRequest createClientRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(clientService.create(createClientRequest));
    }

    @Operation(
            summary = "Обновить клиента",
            description = "Обновляет данные клиента",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UpdateClientRequest.class),
                            examples = @ExampleObject(value = "{ \"id\": \"123e4567-e89b-12d3-a456-426614174000\", \"lastName\": \"Иванов\", \"phone\": \"+375291234567\" }")
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Данные клиента успешно обновлены",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TransactionResponse.class),
                                    examples = @ExampleObject(value = "{ \"status\": \"APPROVED\", \"message\": \"Данные клиента успешно обновлены\", \"timestamp\": \"2025-11-01T12:42:00\" }")
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "Некорректные данные запроса",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TransactionResponse.class),
                                    examples = @ExampleObject(value = "{ \"status\": \"DECLINED\", \"message\": \"lastName: Фамилия не должна быть пустой; phone: Телефон обязателен;\", \"timestamp\": \"2025-11-01T12:42:00\" }")
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Клиент не найден",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TransactionResponse.class),
                                    examples = @ExampleObject(value = "{ \"status\": \"DECLINED\", \"message\": \"Клиент не найден\", \"timestamp\": \"2025-11-01T12:42:00\" }")
                            )
                    ),
                    @ApiResponse(responseCode = "409", description = "Телефон уже используется другим клиентом",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TransactionResponse.class),
                                    examples = @ExampleObject(value = "{ \"status\": \"DECLINED\", \"message\": \"Клиент с телефоном +375291234567 уже существует\", \"timestamp\": \"2025-11-01T12:42:00\" }")
                            )
                    ),
                    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервиса",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TransactionResponse.class),
                                    examples = @ExampleObject(value = "{ \"status\": \"DECLINED\", \"message\": \"Внутренняя ошибка сервиса\", \"timestamp\": \"2025-11-01T12:42:00\" }")
                            )
                    )
            }
    )
    @PutMapping
    public ResponseEntity<TransactionResponse> update(@RequestBody @Valid UpdateClientRequest updateClientRequest) {
        return ResponseEntity.ok(clientService.update(updateClientRequest));
    }

    @Operation(
            summary = "Удалить клиента",
            description = "Удаляет клиента по ID, если у него нет активных счетов",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Клиент успешно удалён",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TransactionResponse.class),
                                    examples = @ExampleObject(value = "{ \"status\": \"APPROVED\", \"message\": \"Клиент был успешно удалён\", \"timestamp\": \"2025-11-01T12:43:00\" }")
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "Клиент имеет активные счета",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TransactionResponse.class),
                                    examples = @ExampleObject(value = "{ \"status\": \"DECLINED\", \"message\": \"Клиент имеет 2 активных счета\", \"timestamp\": \"2025-11-01T12:43:00\" }")
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Клиент не найден",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TransactionResponse.class),
                                    examples = @ExampleObject(value = "{ \"status\": \"DECLINED\", \"message\": \"Клиент не найден\", \"timestamp\": \"2025-11-01T12:43:00\" }")
                            )
                    ),
                    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервиса",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TransactionResponse.class),
                                    examples = @ExampleObject(value = "{ \"status\": \"DECLINED\", \"message\": \"Внутренняя ошибка сервиса\", \"timestamp\": \"2025-11-01T12:43:00\" }")
                            )
                    )
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<TransactionResponse> delete(@PathVariable UUID id) {
        return ResponseEntity.ok(clientService.delete(id));
    }
}