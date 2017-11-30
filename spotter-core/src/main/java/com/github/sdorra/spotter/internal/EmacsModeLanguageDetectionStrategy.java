package com.github.sdorra.spotter.internal;

import java.util.regex.Pattern;

public class EmacsModeLanguageDetectionStrategy extends RegexBasedLanguageDetectionStrategy {

    private static final Pattern PATTERN_LINE = Pattern.compile(".*-\\*-\\s*(.+?)\\s*-\\*-.*(?m:$)");

    private static final Pattern PATTERN_LANG = Pattern.compile(".*(?i:mode)\\s*:\\s*([^\\s;]+)\\s*;*.*");

    public EmacsModeLanguageDetectionStrategy() {
        super(PATTERN_LINE, PATTERN_LANG);
    }
}
