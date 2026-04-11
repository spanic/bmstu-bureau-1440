package com.bmstu_bureau_1440.accounting.io.common.utils;

import dev.tamboui.layout.Constraint;
import dev.tamboui.layout.Layout;
import dev.tamboui.layout.Rect;
import dev.tamboui.text.CharWidth;
import dev.tamboui.toolkit.element.Size;
import dev.tamboui.widgets.table.TableState;

import java.util.List;

public class TuiUtils {

    public static String[] truncateRow(
            String[] cells, Constraint[] widths, int terminalWidth,
            int borderOverhead, int columnSpacing, CharWidth.TruncatePosition position) {
        int available = terminalWidth - borderOverhead - columnSpacing * Math.max(0, widths.length - 1);

        Rect fakeArea = new Rect(0, 0, Math.max(available, 0), 1);

        var rects = Layout.horizontal()
                .constraints(java.util.Arrays.asList(widths))
                .split(fakeArea);

        String[] result = new String[cells.length];

        for (int i = 0; i < cells.length; i++) {
            int w = i < rects.size() ? rects.get(i).width() : 0;
            result[i] = CharWidth.truncateWithEllipsis(cells[i], w, position);
        }

        return result;
    }

    public static Size computePreferredSize(Constraint[] constraints, int availableHeight) {
        int fixedTotal = 0;
        boolean allFixed = true;

        for (Constraint c : constraints) {
            if (c instanceof Constraint.Length) {
                fixedTotal += ((Constraint.Length) c).value();
            } else if (c instanceof Constraint.Min) {
                fixedTotal += ((Constraint.Min) c).value();
            } else if (c instanceof Constraint.Max) {
                fixedTotal += ((Constraint.Max) c).value();
            } else {
                allFixed = false;
            }
        }

        if (allFixed) {
            return Size.heightOnly(fixedTotal);
        }

        if (availableHeight > 0) {
            // Let the Layout engine resolve it — it handles all constraint types
            Rect fakeArea = new Rect(0, 0, 1, availableHeight);
            List<Rect> resolved = Layout.vertical()
                    .constraints(constraints)
                    .split(fakeArea);

            int total = 0;
            for (Rect r : resolved) {
                total += r.height();
            }
            return Size.heightOnly(total);
        }

        // Can't determine — fill available space
        return Size.UNKNOWN;
    }

    public static <T> T getSelectedObject(TableState tableState, List<T> data) {
        final var selectedIdx = tableState.selected();
        return selectedIdx >= 0 && selectedIdx < data.size() ? data.get(selectedIdx) : null;
    }
}
