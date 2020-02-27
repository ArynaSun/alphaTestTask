package com.epam.alfa.repository;

import com.epam.alfa.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ClientRepository extends JpaRepository<Client, Long> {

    @Modifying
    @Query("update Client c set c.riskProfile = ?1 where c.id = ?2")
    void updateClientById(String riskProfile, Long userId);
}
