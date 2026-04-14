package com.bmstu_bureau_1440.accounting.io;

import com.bmstu_bureau_1440.accounting.io.accounts.controller.AccountsTuiController;
import com.bmstu_bureau_1440.accounting.io.accounts.view.AccountDetailsWidget;
import com.bmstu_bureau_1440.accounting.io.accounts.view.AccountsTableWidget;
import com.bmstu_bureau_1440.accounting.io.app.controller.AccountingTuiController;
import com.bmstu_bureau_1440.accounting.io.app.view.TabsWidget;
import com.bmstu_bureau_1440.accounting.io.categories.controller.CategoriesTuiController;
import com.bmstu_bureau_1440.accounting.io.categories.view.CategoriesTableWidget;
import com.bmstu_bureau_1440.accounting.io.categories.view.CategoryDetailsWidget;
import com.bmstu_bureau_1440.accounting.io.operations.controller.OperationsTuiController;
import com.bmstu_bureau_1440.accounting.io.operations.view.AccountsSelectorFilter;
import com.bmstu_bureau_1440.accounting.io.operations.view.OperationsTableWidget;
import dev.tamboui.style.Color;
import dev.tamboui.toolkit.app.ToolkitApp;
import dev.tamboui.toolkit.element.Element;
import org.springframework.stereotype.Component;

import static dev.tamboui.toolkit.Toolkit.*;

@Component
public class AccountingTUI extends ToolkitApp {

    private final AccountingTuiController controller;
    private final AccountsTuiController accountsController;
    private final CategoriesTuiController categoriesController;
    private final OperationsTuiController operationsController;

    private final TabsWidget mainNavigationTabs;
    private final AccountsTableWidget accountsTable;
    private final AccountDetailsWidget accountDetailsWidget;
    private final CategoriesTableWidget categoriesTable;
    private final CategoryDetailsWidget categoryDetailsWidget;
    private final AccountsSelectorFilter filter;
    private final OperationsTableWidget operationsTable;

    public AccountingTUI(AccountingTuiController controller,
                         AccountsTuiController accountsController,
                         CategoriesTuiController categoriesController,
                         OperationsTuiController operationsController) {
        this.controller = controller;
        this.accountsController = accountsController;
        this.categoriesController = categoriesController;
        this.operationsController = operationsController;

        // TODO: seems like it's also a good option to use DI here

        this.mainNavigationTabs = new TabsWidget(controller);

        this.accountsTable = new AccountsTableWidget(accountsController);
        this.accountDetailsWidget = new AccountDetailsWidget(accountsController);

        this.categoriesTable = new CategoriesTableWidget(categoriesController);
        this.categoryDetailsWidget = new CategoryDetailsWidget(categoriesController);

        this.filter = new AccountsSelectorFilter();
        this.operationsTable = new OperationsTableWidget(operationsController);
    }

    @Override
    protected void onStart() {
        accountsController.selectNextAccount();
        categoriesController.selectNextCategory();
    }

    @Override
    protected Element render() {
        return panel(
                "Bank application",
                renderMainTabsNavigation(),
                renderContent(),
                text("Press 'q' to quit").dim()
        )
                .borderColor(Color.YELLOW)
                .rounded();
    }

    private Element renderContent() {
        final Integer currentTabIdx = controller.getMainNavigationTabsState().selected();

        switch (currentTabIdx) {
            case 0 -> {
                return column(
                        renderAccounts(),
                        renderAccountDetails()
                ).fill();
            }
            case 1 -> {
                return column(
                        panel(filter)
                                .id("accounts-filter").focusable().focusedBorderColor(Color.MAGENTA),
                        renderOperations()
                ).fill();
            }
            case 2 -> {
                return column(
                        renderCategories(),
                        renderCategoryDetails()
                ).fill();
            }
            default -> {
                return spacer();
            }
        }
    }

    private Element renderMainTabsNavigation() {
        return mainNavigationTabs;
    }

    private Element renderAccounts() {
        return panel("Accounts", accountsTable)
                .id("accounts")
                .focusable()
                .focusedBorderColor(Color.MAGENTA)
                .fill();
    }

    private Element renderCategories() {
        return panel("Categories", categoriesTable)
                .id("categories")
                .focusable()
                .focusedBorderColor(Color.MAGENTA)
                .fill();
    }

    private Element renderAccountDetails() {
        return panel("Details", accountDetailsWidget);
    }

    private Element renderCategoryDetails() {
        return panel("Details", categoryDetailsWidget);
    }

    private Element renderOperations() {
        return panel("Operations", operationsTable)
                .id("operations")
                .focusable()
                .focusedBorderColor(Color.BLUE);
    }
}