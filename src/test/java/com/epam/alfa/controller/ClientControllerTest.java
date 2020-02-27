package com.epam.alfa.controller;

import com.epam.alfa.entity.Client;
import com.epam.alfa.entity.Risk;
import com.epam.alfa.service.ClientService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClientControllerTest {

    @Mock
    ClientService clientService;

    @InjectMocks
    ClientController clientController;

    @Test
    void retrieveAllClients() {
        List<Client> clients = Arrays.asList(createClientForTest());
        when(clientService.findAll()).thenReturn(clients);

        ResponseEntity<List<Client>> foundedClients = clientController.retrieveAllClients();

        verify(clientService).findAll();
        assertEquals(clients, foundedClients.getBody());
    }

    @Test
    void retrieveClient() {
        Client client = createClientForTest();
        when(clientService.findById(anyLong())).thenReturn(Optional.of(client));

        ResponseEntity<Client> foundedClient = clientController.retrieveClient(1L);

        verify(clientService).findById(1L);
        assertEquals(client, foundedClient.getBody());
    }

    @Test
    void createClient() {
        Client client = createClientForTest();
        when(clientService.create(any(Client.class))).thenReturn(client);

        ResponseEntity<Client> createdClient = clientController.createClient(client);

        verify(clientService).create(client);
        assertEquals(client, createdClient.getBody());
    }

    @Test
    void updateClient() {
        Client client = createClientForTest();
        when(clientService.update(any(Client.class))).thenReturn(Optional.of(client));

        ResponseEntity<Client> updatedClient = clientController.updateClient(client);

        verify(clientService).update(client);
        assertEquals(client, updatedClient.getBody());
    }

    @Test
    void deleteClient() {
        clientController.deleteClient(1L);

        verify(clientService).delete(1L);
    }

    @Test
    void mergeClients() {
        List<Client> clients = createClientsForTest();
        clientController.mergeClients(clients);

        verify(clientService).mergeClients(clients);
    }

    private List<Client> createClientsForTest() {
        Client clientOne = createClientForTest();
        clientOne.setId(1L);
        clientOne.setRiskProfile(Risk.HIGH);
        Client clientTwo = createClientForTest();
        clientTwo.setId(2L);

        return Arrays.asList(clientOne, clientTwo);
    }

    private Client createClientForTest() {
        Client client = new Client();
        client.setRiskProfile(Risk.NORMAL);
        return client;
    }
}