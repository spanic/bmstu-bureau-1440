package com.bmstu_bureau_1440.accounting.io.common.widgets;

import dev.tamboui.layout.Rect;
import dev.tamboui.style.Color;
import dev.tamboui.style.Overflow;
import dev.tamboui.style.Style;
import dev.tamboui.terminal.Frame;
import dev.tamboui.toolkit.element.RenderContext;
import dev.tamboui.toolkit.element.Size;
import dev.tamboui.toolkit.element.StyledElement;
import lombok.AllArgsConstructor;

import static dev.tamboui.toolkit.Toolkit.dialog;
import static dev.tamboui.toolkit.Toolkit.text;

@AllArgsConstructor
public class ConfirmationDialogWidget extends StyledElement<ConfirmationDialogWidget> {

    private Runnable onConfirm;

    private Runnable onCancel;

    @Override
    protected void renderContent(Frame frame, Rect rect, RenderContext renderContext) {
        final var inputDialog = dialog(
                "Are you sure?",
                text("This action cannot be undone! Do you want to continue?").style(Style.EMPTY.fg(Color.RED)).overflow(Overflow.WRAP_WORD),
                text("[Enter] Confirm  [Esc] Cancel").style(Style.EMPTY.fg(Color.RED)).dim()
        )
                .rounded()
                .doubleBorder()
                .minWidth(frame.width() / 2)
                .length(7)
                .borderColor(Color.RED)
                .onConfirm(onConfirm)
                .onCancel(onCancel);

        renderContext.renderChild(inputDialog, frame, frame.area());
    }

    @Override
    public Size preferredSize(int availableWidth, int availableHeight, RenderContext renderContext) {
        return Size.UNKNOWN;
    }
}
