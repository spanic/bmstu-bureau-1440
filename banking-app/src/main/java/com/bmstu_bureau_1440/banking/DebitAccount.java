package com.bmstu_bureau_1440.banking;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class DebitAccount extends Account {

    public DebitAccount(Customer owner) {
        super(owner);
    }

}
