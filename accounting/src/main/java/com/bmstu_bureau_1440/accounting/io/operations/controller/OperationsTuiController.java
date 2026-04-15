package com.bmstu_bureau_1440.accounting.io.operations.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;

import com.bmstu_bureau_1440.accounting.Storage;
import com.bmstu_bureau_1440.accounting.io.common.utils.TuiUtils;
import com.bmstu_bureau_1440.accounting.io.shared.InputFields;
import com.bmstu_bureau_1440.accounting.models.BankAccount;
import com.bmstu_bureau_1440.accounting.models.Category;
import com.bmstu_bureau_1440.accounting.models.Operation;
import com.bmstu_bureau_1440.accounting.services.AccountsService;
import com.bmstu_bureau_1440.accounting.services.CategoriesService;
import com.bmstu_bureau_1440.accounting.services.OperationsService;

import dev.tamboui.widgets.form.FormState;
import dev.tamboui.widgets.table.TableState;
import lombok.Getter;
import lombok.Setter;

@Controller
public class OperationsTuiController {

    private final Storage storage;

    private final OperationsService operationsService;
    private final AccountsService accountsService;
    private final CategoriesService categoriesService;

    @Getter
    private final TableState operationsTableState = new TableState();

    private final Set<String> selectedAccountIds = new HashSet<>();
    private final Set<String> selectedCategoryIds = new HashSet<>();

    @Getter
    @Setter
    private Boolean removeOperationDialogVisible = false;

    @Getter
    private Operation selectedOperation;

    private static final String NO_ACCOUNTS_OPTION = "No accounts";
    private static final String NO_CATEGORIES_OPTION = "No categories";
    private FormState form;
    private List<String> lastAccountOptions = List.of();
    private List<String> lastCategoryOptions = List.of();

    public OperationsTuiController(Storage storage,
            OperationsService operationsService,
            AccountsService accountsService,
            CategoriesService categoriesService) {
        this.storage = storage;
        this.operationsService = operationsService;
        this.accountsService = accountsService;
        this.categoriesService = categoriesService;

        this.form = buildFormState(
                null,
                null,
                BigDecimal.ZERO.toPlainString(),
                StringUtils.EMPTY);
        this.lastAccountOptions = List.copyOf(form.selectField(InputFields.OPERATION_ACCOUNT.getFieldName()).options());
        this.lastCategoryOptions = List
                .copyOf(form.selectField(InputFields.OPERATION_CATEGORY.getFieldName()).options());
    }

    // Queries
    public List<Operation> getOperations() {
        return storage.getOperations()
                .stream()
                .filter(operation -> selectedAccountIds.isEmpty()
                        || selectedAccountIds.contains(operation.getBankAccountId()))
                .filter(operation -> selectedCategoryIds.isEmpty()
                        || selectedCategoryIds.contains(operation.getCategoryId()))
                .toList();
    }

    public List<BankAccount> getAccounts() {
        syncOperationFormSelectOptions();
        selectedAccountIds.retainAll(storage.getAccounts().stream().map(BankAccount::getId).toList());
        return storage.getAccounts();
    }

    public List<Category> getCategories() {
        syncOperationFormSelectOptions();
        selectedCategoryIds.retainAll(storage.getCategories().stream().map(Category::getId).toList());
        return storage.getCategories();
    }

    public FormState getForm() {
        return form;
    }

    public Set<String> getSelectedAccountIds() {
        return Set.copyOf(selectedAccountIds);
    }

    public Set<String> getSelectedCategoryIds() {
        return Set.copyOf(selectedCategoryIds);
    }

    public boolean isAccountSelected(String accountId) {
        return selectedAccountIds.contains(accountId);
    }

    public boolean isCategorySelected(String categoryId) {
        return selectedCategoryIds.contains(categoryId);
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
        syncOperationFormSelectOptions();
        String accountId = form.selectValue(InputFields.OPERATION_ACCOUNT.getFieldName());
        String categoryId = form.selectValue(InputFields.OPERATION_CATEGORY.getFieldName());
        BigDecimal amount = new BigDecimal(getForm().textValue(InputFields.OPERATION_AMOUNT.getFieldName()));
        String description = getForm().textValue(InputFields.OPERATION_DESCRIPTION.getFieldName());

        if (NO_ACCOUNTS_OPTION.equals(accountId) || NO_CATEGORIES_OPTION.equals(categoryId)) {
            return;
        }

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

    public void toggleAccountSelection(String accountId) {
        if (selectedAccountIds.contains(accountId)) {
            selectedAccountIds.remove(accountId);
        } else {
            selectedAccountIds.add(accountId);
        }
        clearOperationSelection();
    }

    public void toggleCategorySelection(String categoryId) {
        if (selectedCategoryIds.contains(categoryId)) {
            selectedCategoryIds.remove(categoryId);
        } else {
            selectedCategoryIds.add(categoryId);
        }
        clearOperationSelection();
    }

    private void updateEditOperationForm() {
        syncOperationFormSelectOptions();

        final String selectedAccountId = selectedOperation == null ? null : selectedOperation.getBankAccountId();
        final String selectedCategoryId = selectedOperation == null ? null : selectedOperation.getCategoryId();

        final int accountIndex = selectedAccountId == null
                ? 0
                : getAccountsSelectValues().indexOf(selectedAccountId);
        if (accountIndex >= 0) {
            form.selectField(InputFields.OPERATION_ACCOUNT.getFieldName()).selectIndex(accountIndex);
        }

        final int categoryIndex = selectedCategoryId == null
                ? 0
                : getCategoriesSelectValues().indexOf(selectedCategoryId);
        if (categoryIndex >= 0) {
            form.selectField(InputFields.OPERATION_CATEGORY.getFieldName()).selectIndex(categoryIndex);
        }

        form.setTextValue(InputFields.OPERATION_DESCRIPTION.getFieldName(),
                selectedOperation == null ? StringUtils.EMPTY : selectedOperation.getDescription());
        form.textField(InputFields.OPERATION_DESCRIPTION.getFieldName()).moveCursorToEnd();

        form.setTextValue(InputFields.OPERATION_AMOUNT.getFieldName(),
                selectedOperation == null ? BigDecimal.ZERO.toPlainString() : selectedOperation.getAmount().toString());
        form.textField(InputFields.OPERATION_AMOUNT.getFieldName()).moveCursorToEnd();
    }

    public void syncOperationFormSelectOptions() {
        final List<String> accountOptions = getAccountsSelectValues();
        final List<String> categoryOptions = getCategoriesSelectValues();

        if (accountOptions.equals(lastAccountOptions) && categoryOptions.equals(lastCategoryOptions)) {
            return;
        }

        final String currentAccountId = form.selectValue(InputFields.OPERATION_ACCOUNT.getFieldName());
        final String currentCategoryId = form.selectValue(InputFields.OPERATION_CATEGORY.getFieldName());
        final String currentAmount = form.textValue(InputFields.OPERATION_AMOUNT.getFieldName());
        final String currentDescription = form.textValue(InputFields.OPERATION_DESCRIPTION.getFieldName());

        this.form = buildFormState(currentAccountId, currentCategoryId, currentAmount, currentDescription);
        this.lastAccountOptions = List.copyOf(accountOptions);
        this.lastCategoryOptions = List.copyOf(categoryOptions);
    }

    private FormState buildFormState(String selectedAccountId, String selectedCategoryId, String amount,
            String description) {
        final List<String> accountOptions = getAccountsSelectValues();
        final List<String> categoryOptions = getCategoriesSelectValues();

        final FormState state = FormState.builder()
                .selectField(InputFields.OPERATION_ACCOUNT.getFieldName(), accountOptions)
                .selectField(InputFields.OPERATION_CATEGORY.getFieldName(), categoryOptions)
                .textField(InputFields.OPERATION_AMOUNT.getFieldName(), amount)
                .textField(InputFields.OPERATION_DESCRIPTION.getFieldName(), description)
                .build();

        final int accountIndex = accountOptions.indexOf(selectedAccountId);
        state.selectField(InputFields.OPERATION_ACCOUNT.getFieldName())
                .selectIndex(accountIndex >= 0 ? accountIndex : 0);

        final int categoryIndex = categoryOptions.indexOf(selectedCategoryId);
        state.selectField(InputFields.OPERATION_CATEGORY.getFieldName())
                .selectIndex(categoryIndex >= 0 ? categoryIndex : 0);

        return state;
    }

    private List<String> getAccountsSelectValues() {
        final List<String> options = new ArrayList<>(storage.getAccounts().stream().map(BankAccount::getId).toList());
        if (options.isEmpty()) {
            options.add(NO_ACCOUNTS_OPTION);
        }
        return options;
    }

    private List<String> getCategoriesSelectValues() {
        final List<String> options = new ArrayList<>(storage.getCategories().stream().map(Category::getId).toList());
        if (options.isEmpty()) {
            options.add(NO_CATEGORIES_OPTION);
        }
        return options;
    }

    public BankAccount getAccountById(String id) {
        return accountsService.getAccountById(id);
    }

    public Category getCategoryById(String id) {
        return categoriesService.getCategoryById(id);
    }

}
