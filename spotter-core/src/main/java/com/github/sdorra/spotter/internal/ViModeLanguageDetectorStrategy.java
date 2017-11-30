package com.github.sdorra.spotter.internal;

import java.util.regex.Pattern;

public class ViModeLanguageDetectorStrategy extends RegexBasedLanguageDetectionStrategy {

    private static final Pattern LINE_PATTERN = Pattern.compile("(?:(?m:\\s|^)vi(?:m[<=>]?\\d+|m)?|[\\t\\x20]*ex)\\s*[:]\\s*(.*)(?m:$)");

    private static final Pattern LANG_PATTERN = Pattern.compile("(?i:filetype|ft|syntax)\\s*=(\\w+)(?:\\s|:|$)");

    public ViModeLanguageDetectorStrategy() {
        super(LINE_PATTERN, LANG_PATTERN);
    }
}