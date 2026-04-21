package com.bmstu_bureau_1440.accounting.io.common;

import dev.tamboui.layout.Constraint;

import java.util.function.Function;

public record Column<T>(String name, Constraint constraint, Function<T, String> valueExtractor) {
}
