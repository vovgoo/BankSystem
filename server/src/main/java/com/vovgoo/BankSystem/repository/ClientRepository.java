package com.vovgoo.BankSystem.repository;

import com.vovgoo.BankSystem.entity.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ClientRepository extends JpaRepository<Client, UUID> {

    Page<Client> findByLastNameContainingIgnoreCase(String lastName, Pageable pageable);

    boolean existsByPhone(String phone);
}