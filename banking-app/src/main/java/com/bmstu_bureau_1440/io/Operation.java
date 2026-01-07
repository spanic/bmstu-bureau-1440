package com.bmstu_bureau_1440.io;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.jline.utils.AttributedStyle;

@RequiredArgsConstructor
@AllArgsConstructor
public enum Operation implements IOperation {

    DEPOSIT("deposit", "Deposit"),
    WITHDRAW("withdraw", "Withdraw"),
    TRANSFER("transfer", "Transfer"),
    CREATE_CUSTOMER("create_customer", "Create customer"),
    OPEN_DEBIT("open_debit", "Open debit account"),
    OPEN_CREDIT("open_credit", "Open credit account"),
    SHOW_ACCOUNTS("show_accounts", "Show accounts"),
    SHOW_TRANSACTIONS("show_transactions", "Show transactions"),
    SHOW_REPORT("show_report", "Show report"),
    EXIT("exit", "Exit", AttributedStyle.BOLD.foreground(AttributedStyle.RED));

    @NonNull
    @Getter
    private final String operation;

    @NonNull
    @Getter
    private final String text;

    @Getter
    private AttributedStyle style;

}
