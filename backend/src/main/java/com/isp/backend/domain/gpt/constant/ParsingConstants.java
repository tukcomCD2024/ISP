package com.isp.backend.domain.gpt.constant;

import java.util.List;

public class ParsingConstants {
    public static final String DATE_REGEX = "(\\d{4}-\\d{2}-\\d{2})";
    public static final String NEW_LINE_REGEX = "\n";
    public static final String CURRENT_DATE = "";
    public static final String COMMA = ", ";
    public static final int BEGIN_INDEX = 3;
    public static final List<String> FILTER_STRINGS = List.of(
            "Message(role=assistant, content=", ")"
            );
}
