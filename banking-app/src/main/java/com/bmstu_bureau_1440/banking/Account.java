package com.bmstu_bureau_1440.banking;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@ToString
public abstract class Account {

    @Getter
    @NonNull
    private final String accountNumber = UUID.randomUUID().toString();
    @Getter
    @NonNull
    @ToString.Exclude
    private final Customer owner;
    @Getter
    @Setter
    private double balance;

    protected Account(@NonNull Customer owner) {
        this.owner = owner;
    }

    public final boolean deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            return true;
        } else {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }
    }

    public boolean withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            return true;
        } else {
            throw new IllegalArgumentException("Withdrawal amount must be positive and less than current balance");
        }
    }

    public final boolean transfer(@NonNull Account to, double amount) {
        var isWithdrawalSuccessful = this.withdraw(amount);
        return isWithdrawalSuccessful && to.deposit(amount);
    }

}
