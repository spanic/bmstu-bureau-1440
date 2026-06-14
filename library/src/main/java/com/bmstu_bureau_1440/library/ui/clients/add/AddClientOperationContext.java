package com.bmstu_bureau_1440.library.ui.clients.add;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bmstu_bureau_1440.library.models.Client;
import com.bmstu_bureau_1440.library.repositories.ClientRepository;

import lombok.Getter;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AddClientOperationContext {

    @Autowired
    @Getter
    private ClientRepository clientRepository;

    @Getter
    private Client client = new Client();

}
