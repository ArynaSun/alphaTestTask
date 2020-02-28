package com.epam.alfa.repository;

import com.epam.alfa.entity.Client;
import com.epam.alfa.entity.Risk;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ClientRepository extends JpaRepository<Client, Long> {

    @Modifying
    @Query("update Client c set c.riskProfile = ?1 where c.id in (?2)")
    void updateClientRiskByIds(Risk riskProfile, List<Long> ids);

    List<Client> findByIdIn(List<Long> inventoryIdList);
}
