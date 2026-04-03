package com.bmstu_bureau_1440.accounting.io.controller;

import com.bmstu_bureau_1440.accounting.Storage;
import com.bmstu_bureau_1440.accounting.models.BankAccount;
import dev.tamboui.widgets.table.TableState;
import dev.tamboui.widgets.tabs.TabsState;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountingTuiController {

    private final Storage storage;

    @Getter
    private final TabsState mainNavigationTabsState = new TabsState(0);

    @Getter
    private final TableState accountsTableState = new TableState();

    @Getter
    private BankAccount selectedBankAccount;

    // Queries
    public List<BankAccount> getAccounts() {
        return storage.getAccounts();
    }

    // Commands
    public void selectPreviousAccount() {
        accountsTableState.selectPrevious();
        setSelectedBankAccount(accountsTableState.selected());
    }

    public void selectNextAccount() {
        accountsTableState.selectNext(storage.getAccounts().size());
        setSelectedBankAccount(accountsTableState.selected());
    }

    private void setSelectedBankAccount(Integer index) {
        final List<BankAccount> accounts = storage.getAccounts();

        if (index >= 0 && index < accounts.size()) {
            selectedBankAccount = accounts.get(index);
        }
    }

}
