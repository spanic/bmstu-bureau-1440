package com.bmstu_bureau_1440.accounting.io.accounts.controller;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;

import com.bmstu_bureau_1440.accounting.Storage;
import com.bmstu_bureau_1440.accounting.io.common.utils.TuiUtils;
import com.bmstu_bureau_1440.accounting.io.shared.InputFields;
import com.bmstu_bureau_1440.accounting.models.BankAccount;
import com.bmstu_bureau_1440.accounting.services.AccountsService;

import dev.tamboui.widgets.form.FormState;
import dev.tamboui.widgets.table.TableState;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Controller
@RequiredArgsConstructor
public class AccountsTuiController {

    private final Storage storage;

    private final AccountsService accountsService;

    @Getter
    private final TableState accountsTableState = new TableState();

    @Getter
    @Setter
    private Boolean removeAccountDialogVisible = false;

    @Getter
    private BankAccount selectedBankAccount;

    @Getter
    private final FormState form = FormState.builder()
            .textField(InputFields.ACCOUNT_NAME.getFieldName(), StringUtils.EMPTY)
            .textField(InputFields.ACCOUNT_BALANCE.getFieldName(), BigDecimal.ZERO.toPlainString())
            .build();

    // Queries
    public List<BankAccount> getAccounts() {
        return storage.getAccounts();
    }

    // Commands
    public void selectPreviousAccount() {
        accountsTableState.selectPrevious();
        selectedBankAccount = TuiUtils.getSelectedObject(accountsTableState, getAccounts());
        updateEditAccountForm();
        setRemoveAccountDialogVisible(false);
    }

    public void selectNextAccount() {
        accountsTableState.selectNext(storage.getAccounts().size());
        selectedBankAccount = TuiUtils.getSelectedObject(accountsTableState, getAccounts());
        updateEditAccountForm();
        setRemoveAccountDialogVisible(false);
    }

    public void clearAccountSelection() {
        accountsTableState.clearSelection();
        selectedBankAccount = null;
        updateEditAccountForm();
    }

    public void createOrUpdateAccount() {
        boolean isNameValid = getForm().validationResult(InputFields.ACCOUNT_NAME.getFieldName()).isValid();
        boolean isBalanceValid = getForm().validationResult(InputFields.ACCOUNT_BALANCE.getFieldName()).isValid();

        if (!isNameValid || !isBalanceValid) {
            return;
        }

        String name = getForm().textValue(InputFields.ACCOUNT_NAME.getFieldName());
        BigDecimal balance = new BigDecimal(getForm().textValue(InputFields.ACCOUNT_BALANCE.getFieldName()));

        if (ObjectUtils.isNotEmpty(selectedBankAccount)) {
            selectedBankAccount.setName(name);
        } else {
            accountsService.addNewBankAccount(name, balance);
            accountsTableState.selectLast(storage.getAccounts().size());
            selectedBankAccount = TuiUtils.getSelectedObject(accountsTableState, getAccounts());
        }
    }

    public void showDeleteConfirmationPopup() {
        if (selectedBankAccount != null) {
            setRemoveAccountDialogVisible(true);
        }
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
            selectedBankAccount = TuiUtils.getSelectedObject(accountsTableState, getAccounts());
            updateEditAccountForm();
        }

    }

    private void updateEditAccountForm() {
        form.setTextValue(InputFields.ACCOUNT_NAME.getFieldName(),
                selectedBankAccount == null ? StringUtils.EMPTY : selectedBankAccount.getName());
        form.textField(InputFields.ACCOUNT_NAME.getFieldName()).moveCursorToEnd();

        form.setTextValue(InputFields.ACCOUNT_BALANCE.getFieldName(),
                selectedBankAccount == null ? BigDecimal.ZERO.toPlainString()
                        : selectedBankAccount.getBalance().toString());
        form.textField(InputFields.ACCOUNT_BALANCE.getFieldName()).moveCursorToEnd();
    }

}
