package com.bmstu_bureau_1440.library.repositories;

import org.springframework.data.repository.CrudRepository;

import com.bmstu_bureau_1440.library.models.Client;

public interface ClientRepository extends CrudRepository<Client, Long> {
}