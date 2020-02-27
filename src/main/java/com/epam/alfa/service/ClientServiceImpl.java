package com.epam.alfa.service;

import com.epam.alfa.entity.Client;
import com.epam.alfa.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    ClientRepository clientRepository;

    @Override
    public Client create(Client client) {
        return clientRepository.save(client);
    }

    @Override
    public Client findById(long id) {
        return clientRepository.findById(id).orElseThrow(() -> new ClientNotFoundException(id));
    }

    @Override
    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    @Transactional
    @Override
    public void update(Client newClient, Long id) {
        clientRepository.updateClientById(newClient.getRiskProfile(), id);
    }

    @Override
    public void delete(Long id) {
        clientRepository.deleteById(id);
    }

    @Override
    public void mergeClients(List<ClientDto> clients) {
        ClientDto.Risk risk = clients.stream().map(ClientDto::getRiskProfile).max(Comparator.comparing(ClientDto.Risk::getRiskLevel)).get();
        List<Client> mergedClients = clients.stream().map(e -> new Client(e.getId(), risk.name())).collect(Collectors.toList());

        for (Client mergedClient : mergedClients) {
            update(mergedClient, mergedClient.getId());
        }
    }
}
