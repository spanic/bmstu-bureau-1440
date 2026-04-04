package com.bmstu_bureau_1440.accounting.io.controller;

import com.bmstu_bureau_1440.accounting.Storage;
import com.bmstu_bureau_1440.accounting.models.BankAccount;
import com.bmstu_bureau_1440.accounting.services.AccountOperationsService;
import dev.tamboui.widgets.form.FormState;
import dev.tamboui.widgets.table.TableState;
import dev.tamboui.widgets.tabs.TabsState;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountingTuiController {

    private final Storage storage;

    private final AccountOperationsService accountsService;

    @Getter
    private final TabsState mainNavigationTabsState = new TabsState(0);

    @Getter
    private final TableState accountsTableState = new TableState();

    @Getter
    @Setter
    private Boolean removeAccountDialogVisible = false;

    @Getter
    private BankAccount selectedBankAccount;

    @Getter
    private final FormState form = FormState.builder()
            .textField("name", "")
            .build();

    // Queries
    public List<BankAccount> getAccounts() {
        return storage.getAccounts();
    }

    // Commands
    public void selectPreviousAccount() {
        accountsTableState.selectPrevious();
        setSelectedBankAccountByIdx(accountsTableState.selected());
        updateEditAccountForm();
        setRemoveAccountDialogVisible(false);
    }

    public void selectNextAccount() {
        accountsTableState.selectNext(storage.getAccounts().size());
        setSelectedBankAccountByIdx(accountsTableState.selected());
        updateEditAccountForm();
        setRemoveAccountDialogVisible(false);
    }

    public void clearAccountSelection() {
        accountsTableState.clearSelection();
        selectedBankAccount = null;
        updateEditAccountForm();
        setRemoveAccountDialogVisible(false);
    }

    public void removeAccount() {
        accountsService.deleteAccount(selectedBankAccount);

        setRemoveAccountDialogVisible(false);

        if (accountsTableState.selected() == storage.getAccounts().size()) {
            if (storage.getAccounts().isEmpty()) {
                clearAccountSelection();
            } else {
                selectPreviousAccount();
            }
        } else {
            setSelectedBankAccountByIdx(accountsTableState.selected());
            updateEditAccountForm();
        }

    }

    private void setSelectedBankAccountByIdx(Integer index) {
        final List<BankAccount> accounts = storage.getAccounts();

        if (index >= 0 && index < accounts.size()) {
            selectedBankAccount = accounts.get(index);
        }
    }

    private void updateEditAccountForm() {
        form.setTextValue("name", ObjectUtils.isEmpty(selectedBankAccount) ?
                StringUtils.EMPTY : selectedBankAccount.getName());
        form.clearValidationResult("name");
        form.textField("name").moveCursorToEnd();
    }
}
