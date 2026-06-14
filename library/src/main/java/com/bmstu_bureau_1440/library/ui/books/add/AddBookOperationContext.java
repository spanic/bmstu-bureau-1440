package com.bmstu_bureau_1440.library.ui.books.add;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bmstu_bureau_1440.library.models.Book;
import com.bmstu_bureau_1440.library.repositories.BookRepository;

import lombok.Getter;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AddBookOperationContext {

    @Autowired
    @Getter
    private BookRepository bookRepository;

    @Getter
    private Book book = new Book();

}
