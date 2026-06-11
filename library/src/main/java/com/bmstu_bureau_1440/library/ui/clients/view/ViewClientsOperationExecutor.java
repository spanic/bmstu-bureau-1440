package com.bmstu_bureau_1440.library.ui.clients.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bmstu_bureau_1440.library.repositories.ClientRepository;
import com.bmstu_bureau_1440.shared.io.IO;

@Component
public class ViewClientsOperationExecutor implements Runnable {

    @Autowired
    private ClientRepository clientRepository;

    @Override
    public void run() {
        var totalClientsQty = clientRepository.count();

        if (totalClientsQty == 0) {
            IO.displayWarning("Читателей не найдено");
            return;
        } else {
            IO.displaySuccess(String.format("Найдено читателей – %d чел.:", totalClientsQty));
        }

        var clients = clientRepository.findAll();
        clients.forEach(client -> System.out.println(client.toString()));
    }

}
