package com.epam.alfa.service;

import com.epam.alfa.entity.Client;

import java.util.List;
import java.util.Optional;

public interface ClientService {

    Client create(Client client);

    Optional<Client> findById(long id);

    List<Client> findAll();

    Optional<Client> update(Client client);

    void delete(Long id);

    void mergeClients(List<Client> clients);
}
