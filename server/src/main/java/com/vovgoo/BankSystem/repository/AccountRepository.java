package com.vovgoo.BankSystem.repository;

import com.vovgoo.BankSystem.entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {
    long countByClientId(UUID clientId);
    List<Account> findByIdIn(List<UUID> ids);

    @Query("""
        SELECT a FROM Account a
        WHERE a.client.id = :clientId
    """)
    Page<Account> findAccountsByClientId(@Param("clientId") UUID clientId, Pageable pageable);
}