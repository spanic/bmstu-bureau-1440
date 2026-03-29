package com.bmstu_bureau_1440.accounting.io;

import com.bmstu_bureau_1440.accounting.Storage;
import dev.tamboui.style.Color;
import dev.tamboui.toolkit.app.ToolkitApp;
import dev.tamboui.toolkit.element.Element;
import org.springframework.stereotype.Component;

import static dev.tamboui.toolkit.Toolkit.*;

@Component
public class AccountingTUI extends ToolkitApp {

    private final TabsWidget mainNavigationTabs = new TabsWidget();
    private final AccountsTableWidget accountsTable;
    private final AccountDetailsWidget accountDetailsWidget = new AccountDetailsWidget();

    public AccountingTUI(Storage storage) {
        this.accountsTable = new AccountsTableWidget(storage);
    }

    @Override
    protected Element render() {
        return panel(
                "Bank application",
                mainNavigationTabs,
                renderContent(mainNavigationTabs.getTabsState().selected()),
                text("Press 'q' to quit").dim()
        )
                .borderColor(Color.YELLOW)
                .rounded();
    }

    private Element renderContent(int currentTab) {
        switch (currentTab) {
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

    private Element renderAccounts() {
        return panel("Accounts", accountsTable)
                .id("accounts")
                .focusable()
                .focusedBorderColor(Color.MAGENTA)
                .fill();
    }

    private Element renderAccountDetails() {
        accountDetailsWidget.setAccount(accountsTable.getSelectedObject());
        return panel("Details", accountDetailsWidget);
    }
}