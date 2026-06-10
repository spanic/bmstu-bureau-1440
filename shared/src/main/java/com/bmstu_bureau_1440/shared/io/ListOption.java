package com.bmstu_bureau_1440.shared.io;

import org.jline.utils.AttributedStyle;

public interface ListOption {
    String getKey();

    String getName();

    default AttributedStyle getStyle() {
        return AttributedStyle.DEFAULT;
    }
}
