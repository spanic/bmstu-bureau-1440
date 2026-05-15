package com.bmstu_bureau_1440.library.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.bmstu_bureau_1440.library.models.Operation;

public interface OperationRepository extends CrudRepository<Operation, Long> {
    List<Operation> findByBookId(Long bookId);
}
