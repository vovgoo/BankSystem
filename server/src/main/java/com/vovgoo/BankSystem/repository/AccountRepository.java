package com.vovgoo.BankSystem.repository;

import com.vovgoo.BankSystem.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {
    long countByClientId(UUID clientId);
}