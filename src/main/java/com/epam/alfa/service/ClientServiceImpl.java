package com.epam.alfa.service;

import com.epam.alfa.entity.Client;
import com.epam.alfa.entity.Risk;
import com.epam.alfa.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
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
    public Optional<Client> findById(long id) {
        return clientRepository.findById(id);
    }

    @Override
    public List<Client> findAll() {
        return clientRepository.findAll();
    }


    @Override
    public Optional<Client> update(Client newClient) {

        Client client = clientRepository.findById(newClient.getId())
                                        .map(e -> clientRepository.save(newClient))
                                        .orElseThrow(() -> new ServiceException("No such client to be updated"));
        return Optional.of(client);
    }

    @Override
    public void delete(Long id) {
        clientRepository.deleteById(id);
    }

    @Transactional
    @Override
    public void mergeClients(List<Client> clients) {
        List<Long> idList = clients.stream().map(Client::getId).collect(Collectors.toList());
        List<Client> clientsInDb = clientRepository.findByIdIn(idList);
        Risk maxRisk =
                clients.stream()
                        .map(Client::getRiskProfile)
                        .max(Comparator.comparing(Enum::ordinal))
                        .orElseThrow(() -> new ServiceException("No max client risk"));

        List<Long> ids = clientsInDb
                .stream()
                .filter(e -> (e.getRiskProfile() != maxRisk))
                .map(Client::getId)
                .collect(Collectors.toList());

        clientRepository.updateClientRiskByIds(maxRisk, ids);
    }
}
