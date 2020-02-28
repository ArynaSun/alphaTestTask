package com.epam.alfa.controller;

import com.epam.alfa.entity.Client;
import com.epam.alfa.entity.Risk;
import com.epam.alfa.service.ClientService;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ClientController.class)
class ClientControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ClientService clientService;

    @Test
    void retrieveAllClients() throws Exception {
        Client client = createClientForTest();

        List<Client> allEmployees = Arrays.asList(client);

        given(clientService.findAll()).willReturn(allEmployees);

        mvc.perform(get("/clients")
                .contentType(MediaType.APPLICATION_JSON))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$", hasSize(1)))
           .andExpect(jsonPath("$[0].riskProfile", is(client.getRiskProfile().name())));
    }

    @Test
    void retrieveClient() throws Exception {
        Client client = createClientForTest();
        given(clientService.findById(1L)).willReturn(Optional.of(client));

        mvc.perform(get("/clients/{id}", 1L).contentType(MediaType.APPLICATION_JSON))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.*").exists())
           .andExpect(jsonPath("$.*", notNullValue()))
           .andExpect(jsonPath("$.*", hasSize(2)))
           .andExpect(jsonPath("$.riskProfile", is(client.getRiskProfile().name())));
    }

    @Test
    void createClient() throws Exception {
        Client client = createClientForTest();
        Client client2 = createClientForTest();
        client2.setId(1);
        given(clientService.create(client)).willReturn(client2);

        mvc.perform(post("/clients").contentType(MediaType.APPLICATION_JSON).content(
                "{\"riskProfile\": \"NORMAL\" }"))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.*").exists())
           .andExpect(jsonPath("$", notNullValue()))
           .andExpect(jsonPath("$.riskProfile", is(client.getRiskProfile().name())));
    }

    @Test
    void updateClient() throws Exception {
        Client client = createClientForTest();
        Client client2 = createClientForTest();
        given(clientService.update(client)).willReturn(Optional.of(client2));

        mvc.perform(put("/clients/one").contentType(MediaType.APPLICATION_JSON).content(
                "{\"riskProfile\": \"NORMAL\" }"))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.*").exists())
           .andExpect(jsonPath("$", notNullValue()))
           .andExpect(jsonPath("$.riskProfile", is(client.getRiskProfile().name())));
    }

    @Test
    void deleteClient() throws Exception {
        willDoNothing().given(clientService).delete(1L);

        mvc.perform(delete("/clients/{id}", 1L).contentType(MediaType.APPLICATION_JSON))
           .andExpect(status().isOk());

        verify(clientService).delete(1L);
    }

    @Test
    void mergeClients() throws Exception {
        Client client = createClientForTest();
        client.setId(1);
        Client client2 = createClientForTest();
        client2.setId(2);
        client2.setRiskProfile(Risk.LOW);
        List<Client> clients = Arrays.asList(client, client2);
        willDoNothing().given(clientService).mergeClients(clients);

        mvc.perform(put("/clients").contentType(MediaType.APPLICATION_JSON)
                                   .content("[ { \"id\": 1, \"riskProfile\": \"NORMAL\"  }, { \"id\": 2,  \"riskProfile\": \"HIGH\" }]"))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.*").exists())
           .andExpect(jsonPath("$", notNullValue()))
           .andExpect(jsonPath("$[0].riskProfile", is(client.getRiskProfile().name())));
    }

    private Client createClientForTest() {
        Client client = new Client();
        client.setRiskProfile(Risk.NORMAL);
        return client;
    }
}