package com.bmstu_bureau_1440.accounting.io;

import dev.tamboui.layout.Constraint;
import dev.tamboui.layout.Layout;
import dev.tamboui.layout.Rect;
import dev.tamboui.text.CharWidth;

public final class EllipsisHelper {
    private EllipsisHelper() {
    }

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
}
