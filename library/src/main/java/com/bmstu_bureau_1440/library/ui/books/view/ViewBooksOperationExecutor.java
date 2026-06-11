package com.bmstu_bureau_1440.library.ui.books.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bmstu_bureau_1440.library.repositories.BookRepository;

@Component
public class ViewBooksOperationExecutor implements Runnable {

    @Autowired
    private BookRepository bookRepository;

    @Override
    public void run() {
        var books = bookRepository.findAll();
        books.forEach(book -> System.out.println(book.toString()));
    }

}
