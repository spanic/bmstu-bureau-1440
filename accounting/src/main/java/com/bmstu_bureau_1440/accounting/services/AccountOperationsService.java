package com.bmstu_bureau_1440.accounting.services;

import com.bmstu_bureau_1440.accounting.Storage;
import com.bmstu_bureau_1440.accounting.models.BankAccount;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AccountOperationsService {

    private final Storage storage;

    public BankAccount addNewBankAccount(String name, BigDecimal balance) {
        BankAccount account = new BankAccount(name, ObjectUtils.isEmpty(balance) ? BigDecimal.ZERO : balance);
        storage.getAccounts().add(account);
        return account;
    }

    public void renameAccount(BankAccount account, String name) {
        account.setName(name);
    }

    public boolean deleteAccount(BankAccount account) {
        return storage.getAccounts().remove(account);
    }
}
