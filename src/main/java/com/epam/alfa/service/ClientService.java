package com.epam.alfa.service;

import com.epam.alfa.entity.Client;

import java.util.List;

public interface ClientService {

    Client create(Client client);

    Client findById(long id);

    List<Client> findAll();

    void update(Client client, Long id);

    void delete (Long id);

    void mergeClients(List<ClientDto> clients);
}
