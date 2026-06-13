package com.bmstu_bureau_1440.library.ui.operations.add;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bmstu_bureau_1440.library.models.Book;
import com.bmstu_bureau_1440.library.models.Operation;
import com.bmstu_bureau_1440.library.repositories.BookRepository;
import com.bmstu_bureau_1440.library.repositories.ClientRepository;
import com.bmstu_bureau_1440.library.repositories.OperationRepository;

import lombok.Getter;
import lombok.Setter;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AddOperationContext {

    @Autowired
    @Getter
    private OperationRepository operationRepository;

    @Autowired
    @Getter
    private ClientRepository clientRepository;

    @Autowired
    @Getter
    private BookRepository bookRepository;

    @Getter
    private Operation operation = new Operation();

    @Getter
    @Setter
    private Book book;

}
