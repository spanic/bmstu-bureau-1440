package com.bmstu_bureau_1440.library.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bmstu_bureau_1440.library.AbstractIntegrationTest;
import com.bmstu_bureau_1440.library.models.Client;

public class ClientRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private OperationRepository operationRepository;

    @BeforeEach
    void setUp() {
        operationRepository.deleteAll();
        clientRepository.deleteAll();
    }

    @Test
    void saveAndFindById() {
        var client = new Client(null, "Иван Петров", "ivan.petrov@example.com", null);
        var saved = clientRepository.save(client);

        var found = clientRepository.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getCreatedAt()).isNotNull();
    }

    @Test
    void findAllReturnsAllClients() {
        clientRepository.save(new Client(null, "Иван Петров", "ivan.petrov@example.com", null));
        clientRepository.save(new Client(null, "Мария Сидорова", "maria.sidorova@example.com", null));

        var all = clientRepository.findAll();

        assertThat(all).hasSize(2);
    }

}
