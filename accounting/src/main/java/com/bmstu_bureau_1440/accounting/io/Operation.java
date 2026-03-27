package com.bmstu_bureau_1440.accounting.io;

import com.bmstu_bureau_1440.shared.io.IOperation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.jline.utils.AttributedStyle;

@RequiredArgsConstructor
@AllArgsConstructor
public enum Operation implements IOperation {

    SEE_CATEGORIES("see_categories", "See categories"),
    SEE_ACCOUNTS("see_accounts", "See accounts"),
    SEE_OPERATIONS("see_operations", "See operations"),
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
