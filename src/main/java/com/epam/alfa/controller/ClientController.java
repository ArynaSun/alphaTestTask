package com.epam.alfa.controller;

import com.epam.alfa.entity.Client;
import com.epam.alfa.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ClientController {

    @Autowired
    private ClientService clientService;

    @GetMapping("/clients")
    List<Client> retrieveAllClients() {
        return clientService.findAll();
    }

    @GetMapping("/clients/{id}")
    Client retrieveClient(@PathVariable Long id) {
        return clientService.findById(id);
    }

    @PostMapping("/clients")
    Client createClient(@RequestBody Client newClient) {
        return clientService.create(newClient);
    }

    @PutMapping("/clients/{id}")
    void updateClient(@RequestBody Client newClient, @PathVariable Long id) {
        clientService.update(newClient, id);
    }

    @DeleteMapping("/clients/{id}")
    void deleteClient(@PathVariable Long id) {
        clientService.delete(id);
    }
}
