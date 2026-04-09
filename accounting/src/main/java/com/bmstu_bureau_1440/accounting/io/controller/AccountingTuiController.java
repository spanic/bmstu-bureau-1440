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

import java.math.BigDecimal;
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
            .textField("balance", "0")
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
    }

    public void createOrUpdateAccount() {
        boolean isNameValid = getForm().validationResult("name").isValid();
        boolean isBalanceValid = getForm().validationResult("balance").isValid();

        if (!isNameValid || !isBalanceValid) {
            return;
        }

        String name = getForm().textValue("name");
        BigDecimal balance = new BigDecimal(getForm().textValue("balance"));

        if (ObjectUtils.isNotEmpty(selectedBankAccount)) {
            selectedBankAccount.setName(name);
            selectedBankAccount.setBalance(balance);
        } else {
            accountsService.addNewBankAccount(name, balance);
            accountsTableState.selectLast(storage.getAccounts().size());
            setSelectedBankAccountByIdx(accountsTableState.selected());
        }
    }

    public void focusOnEmptyAccountDetails() {
        clearAccountSelection();
    }

    public void removeAccount() {
        accountsService.deleteAccount(selectedBankAccount);

        setRemoveAccountDialogVisible(false);

        if (accountsTableState.selected() == storage.getAccounts().size()) {
            if (storage.getAccounts().isEmpty()) {
                clearAccountSelection();
                setRemoveAccountDialogVisible(false);
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
        form.setTextValue("name", selectedBankAccount == null ?
                StringUtils.EMPTY : selectedBankAccount.getName());
        form.textField("name").moveCursorToEnd();

        form.setTextValue("balance", selectedBankAccount == null ?
                "0" : selectedBankAccount.getBalance().toString());
        form.textField("balance").moveCursorToEnd();
    }
}
