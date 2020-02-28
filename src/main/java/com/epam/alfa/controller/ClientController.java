package com.epam.alfa.controller;

import com.epam.alfa.entity.Client;
import com.epam.alfa.service.ClientService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequestMapping("/clients")
@RestController
public class ClientController {

    @Autowired
    private ClientService clientService;

    @GetMapping
    ResponseEntity<List<Client>> retrieveAllClients() {
        return ResponseEntity.ok(clientService.findAll());
    }

    @GetMapping("/{id}")
    ResponseEntity<Client> retrieveClient(@PathVariable Long id) {
        return clientService.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(produces = APPLICATION_JSON_VALUE)
    ResponseEntity<Client> createClient(@RequestBody Client newClient) {
        return ResponseEntity.ok(clientService.create(newClient));
    }

    @PutMapping("/one")
    ResponseEntity<Client> updateClient(@RequestBody Client newClient) {
        return clientService.update(newClient).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    void deleteClient(@PathVariable Long id) {
        clientService.delete(id);
    }

    @PutMapping
    ResponseEntity<List<Client>> mergeClients(@RequestBody List<Client> clients) {
        clientService.mergeClients(clients);
        return ResponseEntity.ok(clients);
    }

}
