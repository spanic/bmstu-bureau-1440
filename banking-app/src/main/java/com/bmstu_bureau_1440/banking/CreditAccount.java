package com.bmstu_bureau_1440.banking;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode(callSuper = true)
public class CreditAccount extends Account {

    @Getter
    private final double creditLimit;

    public CreditAccount(Customer owner, double creditLimit) {
        super(owner);

        if (creditLimit < 0) {
            throw new IllegalArgumentException("Credit limit must be positive");
        }
        this.creditLimit = creditLimit;
    }

    @Override
    public boolean withdraw(double amount) {
        var balance = getBalance();

        if (amount > 0 && amount <= balance + creditLimit) {
            setBalance(balance - amount);
            return true;
        } else {
            throw new IllegalArgumentException("Withdrawal amount must be positive and less than or equal to balance + credit limit");
        }
    }

}
