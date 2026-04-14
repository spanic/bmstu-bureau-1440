package com.bmstu_bureau_1440.accounting.io.accounts.view;

import com.bmstu_bureau_1440.accounting.io.accounts.controller.AccountsTuiController;
import com.bmstu_bureau_1440.accounting.io.common.Column;
import com.bmstu_bureau_1440.accounting.io.common.widgets.AbstractTableWidget;
import com.bmstu_bureau_1440.accounting.io.common.widgets.ConfirmationDialogWidget;
import com.bmstu_bureau_1440.accounting.models.BankAccount;
import dev.tamboui.layout.Constraint;
import dev.tamboui.layout.Rect;
import dev.tamboui.terminal.Frame;
import dev.tamboui.toolkit.element.RenderContext;
import dev.tamboui.toolkit.event.EventResult;
import dev.tamboui.tui.event.KeyEvent;
import dev.tamboui.widgets.table.TableState;

import java.util.List;
import java.util.function.Function;

public class AccountsTableWidget extends AbstractTableWidget<BankAccount, AccountsTuiController> {

    public AccountsTableWidget(AccountsTuiController controller) {
        super(controller);
    }

    @Override
    protected Function<AccountsTuiController, TableState> getStateProvider() {
        return AccountsTuiController::getAccountsTableState;
    }

    @Override
    protected List<Column<BankAccount>> getColumns() {
        return List.of(
                new Column<>("ID", Constraint.percentage(40), BankAccount::getId),
                new Column<>("Name", Constraint.fill(), BankAccount::getName),
                new Column<>("Balance (₽)", Constraint.percentage(20), account -> account.getBalance().toString())
        );
    }

    @Override
    protected Function<AccountsTuiController, List<BankAccount>> getDataProvider() {
        return AccountsTuiController::getAccounts;
    }

    @Override
    protected void renderContent(Frame frame, Rect rect, RenderContext renderContext) {
        super.renderContent(frame, rect, renderContext);

        if (controller.getRemoveAccountDialogVisible()) {
            renderContext.renderChild(
                    new ConfirmationDialogWidget(controller::removeAccount, () -> {
                        controller.setRemoveAccountDialogVisible(false);
                    }),
                    frame, rect
            );
        }
    }

    @Override
    public EventResult handleKeyEvent(KeyEvent event, boolean focused) {
        if (!focused) {
            return EventResult.UNHANDLED;
        }

        if (event.isUp()) {
            controller.selectPreviousAccount();
            return EventResult.HANDLED;
        } else if (event.isDown()) {
            controller.selectNextAccount();
            return EventResult.HANDLED;
        } else if (event.isChar('d')) {
            controller.showDeleteConfirmationPopup();
            return EventResult.HANDLED;
        } else if (event.isChar('c')) {
            controller.clearAccountSelection();
            return EventResult.HANDLED;
        } else {
            return EventResult.UNHANDLED;
        }
    }
}
