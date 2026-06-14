package com.bmstu_bureau_1440.library.ui.statistics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bmstu_bureau_1440.library.repositories.BookRepository;
import com.bmstu_bureau_1440.shared.io.IO;

@Component
public class ShowTrendingBooksOperationExecutor implements Runnable {

    @Autowired
    private BookRepository bookRepository;

    @Override
    public void run() {

        var trendingBooks = bookRepository.findTop10TrendingBooks();

        if (trendingBooks.isEmpty()) {
            IO.displayWarning("Популярных книг не найдено");
            return;
        } else {
            IO.displaySuccess(String.format("Найдено популярных книг – %d шт.:", trendingBooks.size()));
        }

        trendingBooks.forEach(book -> System.out
                .println(String.format("%s – взяли %d раз(-a)", book.toString(),
                        book.getTotalWithdrawOperationsCount())));

    }

}