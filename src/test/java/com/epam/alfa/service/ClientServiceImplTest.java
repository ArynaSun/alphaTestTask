package com.epam.alfa.service;

import com.epam.alfa.entity.Client;
import com.epam.alfa.entity.Risk;
import com.epam.alfa.repository.ClientRepository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
class ClientServiceImplTest {

    @Mock
    ClientRepository clientRepository;

    @InjectMocks
    ClientService clientService = new ClientServiceImpl();

    @Test
    void create() {
        Client client = retrieveClient();
        when(clientRepository.save(any(Client.class))).thenReturn(client);

        Client createdClient = clientService.create(client);

        verify(clientRepository).save(client);
        assertEquals(client, createdClient);
    }

    @Test
    void findById() {
        Client client = retrieveClient();
        when(clientRepository.findById(anyLong())).thenReturn(Optional.of(client));

        Optional<Client> foundedClient = clientService.findById(1);

        verify(clientRepository).findById(1L);
        assertEquals(Optional.of(client), foundedClient);
    }

    @Test
    void findAll() {
        List<Client> clients = Arrays.asList(retrieveClient());
        when(clientRepository.findAll()).thenReturn(clients);

        List<Client> foundedClients = clientService.findAll();

        assertEquals(clients, foundedClients);
        verify(clientRepository).findAll();
    }

    @Test
    void update() {
        Client clientToSave = retrieveClient();
        Client savedClient = retrieveClient();
        Client clientDb = retrieveClient();

        when(clientRepository.findById(anyLong())).thenReturn(Optional.of(clientDb));
        when(clientRepository.save(clientToSave)).thenReturn(savedClient);

        clientService.update(clientToSave);

        verify(clientRepository).save(clientToSave);
    }

    @Test
    void updateNonExistingClient() {
        Client clientToSave = retrieveClient();

        when(clientRepository.findById(anyLong())).thenReturn(Optional.empty());

        Assertions.assertThrows(ServiceException.class, () -> {
            clientService.update(clientToSave);
        });
    }

    @Test
    void delete() {
        clientService.delete(1L);

        verify(clientRepository, times(1)).deleteById(1L);
    }

    @Test
    void mergeClients() {
        List<Client> clients = retrieveClients();
        List<Long> id = Arrays.asList(2L);

        when(clientRepository.findByIdIn(anyList())).thenReturn(clients);

        clientService.mergeClients(clients);

        verify(clientRepository).updateClientRiskByIds(Risk.HIGH, id);
    }

    private Client retrieveClient() {
        Client client = new Client();
        client.setRiskProfile(Risk.NORMAL);
        return client;
    }

    private List<Client> retrieveClients() {
        Client clientOne = retrieveClient();
        clientOne.setId(1L);
        clientOne.setRiskProfile(Risk.HIGH);
        Client clientTwo = retrieveClient();
        clientTwo.setId(2L);

        return Arrays.asList(clientOne, clientTwo);
    }
}