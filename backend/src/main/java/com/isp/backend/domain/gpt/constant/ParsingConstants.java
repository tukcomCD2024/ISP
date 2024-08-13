package com.isp.backend.domain.gpt.constant;

import lombok.Getter;

import javax.swing.*;

@Getter
public enum ParsingConstants {
    COMMA(", "),
    ENTRY_SEPARATOR("<"),
    LINE_SEPARATOR("\n"),
    DATE_SUFFIX(">"),
    PRICE_PREFIX("("),
    PRICE_SUFFIX(")"),
    DETAIL_PREFIX("- "),
    DETAIL_SUFFIX(": "),
    DEFAULT_PRICE(0.0),
    DEFAULT_COORDINATE(0.0);
    private final String stringValue;
    private final Double doubleValue;

    ParsingConstants(String stringValue) {
        this.stringValue = stringValue;
        this.doubleValue = null;
    }

    ParsingConstants(Double doubleValue) {
        this.stringValue = null;
        this.doubleValue = doubleValue;
    }

}