package com.bmstu_bureau_1440.library.ui.books.find;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bmstu_bureau_1440.library.repositories.BookRepository;

import lombok.Getter;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class FindBookOperationContext {

    @Autowired
    @Getter
    private BookRepository bookRepository;

    String title;

}
