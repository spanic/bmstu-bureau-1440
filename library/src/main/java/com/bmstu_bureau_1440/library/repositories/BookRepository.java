package com.bmstu_bureau_1440.library.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.bmstu_bureau_1440.library.models.Book;

public interface BookRepository extends CrudRepository<Book, Long> {
    List<Book> findByTitleContainingIgnoreCase(String title);

    List<Book> findByAvailableTrue();
}