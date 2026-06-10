package com.bmstu_bureau_1440.library.repositories;

import java.util.List;

import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.data.repository.CrudRepository;

import com.bmstu_bureau_1440.library.models.Book;
import com.bmstu_bureau_1440.library.models.Operation;

public interface OperationRepository extends CrudRepository<Operation, Long> {
    List<Operation> findByBook(AggregateReference<Book, Long> book);
}
