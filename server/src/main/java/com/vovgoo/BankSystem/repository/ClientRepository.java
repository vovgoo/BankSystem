package com.vovgoo.BankSystem.repository;

import com.vovgoo.BankSystem.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ClientRepository extends JpaRepository<Client, UUID> {

}