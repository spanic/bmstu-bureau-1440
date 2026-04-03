package com.bmstu_bureau_1440.accounting.io;

import com.bmstu_bureau_1440.accounting.io.controller.AccountingTuiController;
import dev.tamboui.style.Color;
import dev.tamboui.toolkit.app.ToolkitApp;
import dev.tamboui.toolkit.element.Element;
import org.springframework.stereotype.Component;

import static dev.tamboui.toolkit.Toolkit.*;

@Component
public class AccountingTUI extends ToolkitApp {

    private final AccountingTuiController controller;

    private final TabsWidget mainNavigationTabs;
    private final AccountsTableWidget accountsTable;
    private final AccountDetailsWidget accountDetailsWidget;

    public AccountingTUI(AccountingTuiController controller) {

        this.controller = controller;

        this.mainNavigationTabs = new TabsWidget(controller);
        this.accountsTable = new AccountsTableWidget(controller);
        this.accountDetailsWidget = new AccountDetailsWidget(controller);
    }

    @Override
    protected void onStart() {
        controller.selectNextAccount();
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

    private Element renderAccountDetails() {
        return panel("Details", accountDetailsWidget);
    }
}