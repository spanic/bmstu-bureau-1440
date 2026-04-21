package com.bmstu_bureau_1440.accounting.services;

import java.math.BigDecimal;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import com.bmstu_bureau_1440.accounting.Storage;
import com.bmstu_bureau_1440.accounting.models.BankAccount;
import com.bmstu_bureau_1440.accounting.models.Operation;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountsService {

    private final Storage storage;

    public BankAccount addNewBankAccount(String name, BigDecimal balance) {
        final BankAccount account = new BankAccount(name, ObjectUtils.isEmpty(balance) ? BigDecimal.ZERO : balance);
        storage.getAccounts().add(account);
        return account;
    }

    public void renameAccount(BankAccount account, String name) {
        account.setName(name);
    }

    public void applyOperation(@NonNull Operation operation, boolean isRollback) {
        final BankAccount bankAccount = getAccountById(operation.getBankAccountId());
        final BigDecimal operationAmount = isRollback ? operation.getAmount().negate() : operation.getAmount();
        final BigDecimal updatedBalance = bankAccount.getBalance().add(operationAmount);

        if (updatedBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Cannot apply operation: account balance cannot be negative");
        }

        bankAccount.setBalance(updatedBalance);
    }

    public boolean deleteAccount(BankAccount account) {
        return storage.getAccounts().remove(account);
    }

    public BankAccount getAccountById(String id) {
        return storage.getAccounts().stream()
                .filter(account -> account.getId().equals(id))
                .findFirst()
                .orElseThrow();
    }
}
