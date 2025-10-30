package com.vovgoo.BankSystem.repository;

import com.vovgoo.BankSystem.dto.client.response.ClientResponse;
import com.vovgoo.BankSystem.entity.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface ClientRepository extends JpaRepository<Client, UUID> {

    @Query("""
        SELECT c.id AS id, c.lastName AS lastName, c.phone AS phone, COALESCE(SUM(a.balance), 0) AS totalBalance
        FROM Client c
        LEFT JOIN c.accounts a
        WHERE (:lastName IS NULL OR LOWER(c.lastName) LIKE LOWER(CONCAT('%', :lastName, '%')))
        GROUP BY c.id
    """)
    Page<ClientResponse> findClientSummaries(@Param("lastName") String lastName, Pageable pageable);

    @Query("""
        SELECT c FROM Client c
        LEFT JOIN FETCH c.accounts
        WHERE c.id = :id
    """)
    Optional<Client> findByIdWithAccounts(@Param("id") UUID id);

    boolean existsByPhone(String phone);
}