package com.bmstu_bureau_1440.accounting.io.utils;

import dev.tamboui.layout.Constraint;
import dev.tamboui.layout.Layout;
import dev.tamboui.layout.Rect;
import dev.tamboui.toolkit.element.Size;

import java.util.List;

public class TuiUtils {

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
}
