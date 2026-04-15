package com.bmstu_bureau_1440.accounting.io.operations.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;

import com.bmstu_bureau_1440.accounting.Storage;
import com.bmstu_bureau_1440.accounting.io.common.utils.TuiUtils;
import com.bmstu_bureau_1440.accounting.io.shared.InputFields;
import com.bmstu_bureau_1440.accounting.models.BankAccount;
import com.bmstu_bureau_1440.accounting.models.Category;
import com.bmstu_bureau_1440.accounting.models.Operation;
import com.bmstu_bureau_1440.accounting.services.OperationsService;

import dev.tamboui.widgets.form.FormState;
import dev.tamboui.widgets.list.ListState;
import dev.tamboui.widgets.table.TableState;
import lombok.Getter;
import lombok.Setter;

@Controller
public class OperationsTuiController {

    private final Storage storage;

    private final OperationsService operationsService;

    @Getter
    private final TableState operationsTableState = new TableState();

    @Getter
    private final ListState accountsFilterListState = new ListState();

    private final Set<String> selectedAccountIds = new LinkedHashSet<>();

    @Getter
    @Setter
    private Boolean removeOperationDialogVisible = false;

    @Getter
    private Operation selectedOperation;

    @Getter
    private final FormState form;

    public OperationsTuiController(Storage storage, OperationsService operationsService) {
        this.storage = storage;
        this.operationsService = operationsService;

        this.form = FormState.builder()
                .selectField(InputFields.OPERATION_ACCOUNT.getFieldName(), getAccountsSelectValues())
                .selectField(InputFields.OPERATION_CATEGORY.getFieldName(), getCategoriesSelectValues())
                .textField(InputFields.OPERATION_AMOUNT.getFieldName(), BigDecimal.ZERO.toPlainString())
                .textField(InputFields.OPERATION_DESCRIPTION.getFieldName(), StringUtils.EMPTY)
                .build();
    }

    // Queries
    public List<Operation> getOperations() {
        return storage.getOperations();
    }

    public List<BankAccount> getAccounts() {
        selectedAccountIds.retainAll(storage.getAccounts().stream().map(BankAccount::getId).toList());
        return storage.getAccounts();
    }

    public Set<String> getSelectedAccountIds() {
        return Set.copyOf(selectedAccountIds);
    }

    // Commands
    public void selectPreviousOperation() {
        operationsTableState.selectPrevious();
        selectedOperation = TuiUtils.getSelectedObject(operationsTableState, getOperations());
        updateEditOperationForm();
        setRemoveOperationDialogVisible(false);
    }

    public void selectNextOperation() {
        operationsTableState.selectNext(getOperations().size());
        selectedOperation = TuiUtils.getSelectedObject(operationsTableState, getOperations());
        updateEditOperationForm();
        setRemoveOperationDialogVisible(false);
    }

    public void clearOperationSelection() {
        operationsTableState.clearSelection();
        selectedOperation = null;
        updateEditOperationForm();
    }

    public void createOrUpdateOperation() {
        String accountId = form.selectValue(InputFields.OPERATION_ACCOUNT.getFieldName());
        String categoryId = form.selectValue(InputFields.OPERATION_CATEGORY.getFieldName());
        BigDecimal amount = new BigDecimal(getForm().textValue(InputFields.OPERATION_AMOUNT.getFieldName()));
        String description = getForm().textValue(InputFields.OPERATION_DESCRIPTION.getFieldName());

        if (ObjectUtils.isNotEmpty(selectedOperation)) {
            selectedOperation.setDescription(description);
        } else {
            operationsService.addNewOperation(accountId, categoryId, amount, description);
            operationsTableState.selectLast(getOperations().size());
            selectedOperation = TuiUtils.getSelectedObject(operationsTableState, getOperations());
        }
    }

    public void showDeleteConfirmationPopup() {
        if (selectedOperation != null) {
            setRemoveOperationDialogVisible(true);
        }
    }

    public void removeOperation() {
        operationsService.deleteOperation(selectedOperation);

        setRemoveOperationDialogVisible(false);

        if (operationsTableState.selected() == storage.getOperations().size()) {
            if (storage.getOperations().isEmpty()) {
                clearOperationSelection();
            } else {
                selectPreviousOperation();
            }
        } else {
            selectedOperation = TuiUtils.getSelectedObject(operationsTableState, getOperations());
            updateEditOperationForm();
        }

    }

    public boolean isAccountSelected(String accountId) {
        return selectedAccountIds.contains(accountId);
    }

    public void toggleAccountSelection(String accountId) {
        if (selectedAccountIds.contains(accountId)) {
            selectedAccountIds.remove(accountId);
            return;
        }
        selectedAccountIds.add(accountId);
    }

    private void updateEditOperationForm() {
        form.setTextValue(InputFields.OPERATION_DESCRIPTION.getFieldName(),
                selectedOperation == null ? StringUtils.EMPTY : selectedOperation.getDescription());
        form.textField(InputFields.OPERATION_DESCRIPTION.getFieldName()).moveCursorToEnd();

        form.setTextValue(InputFields.OPERATION_AMOUNT.getFieldName(),
                selectedOperation == null ? BigDecimal.ZERO.toPlainString() : selectedOperation.getAmount().toString());
        form.textField(InputFields.OPERATION_AMOUNT.getFieldName()).moveCursorToEnd();
    }

    private List<String> getAccountsSelectValues() {
        final List<String> options = storage.getAccounts().stream()
                .map(BankAccount::getId)
                .collect(Collectors.toCollection(ArrayList::new));

        if (options.isEmpty()) {
            options.add("No accounts");
        }

        return options;
    }

    private List<String> getCategoriesSelectValues() {
        final List<String> options = storage.getCategories().stream()
                .map(Category::getId)
                .collect(Collectors.toCollection(ArrayList::new));

        if (options.isEmpty()) {
            options.add("No categories");
        }

        return options;
    }

}
