package com.github.sdorra.spotter.internal;

import com.github.sdorra.spotter.Language;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ViModeLanguageDetectorStrategy extends StringContentBasedLanguageDetectionStrategy {

    private static final Pattern LINE_PATTERN = Pattern.compile("(?:(?m:\\s|^)vi(?:m[<=>]?\\d+|m)?|[\\t\\x20]*ex)\\s*[:]\\s*(.*)(?m:$)");

    private static final Pattern LANG_PATTERN = Pattern.compile("(?i:filetype|ft|syntax)\\s*=(\\w+)(?:\\s|:|$)");

    @Override
    protected Optional<Language> detectByContent(String content) {
        String line = firstGroup(LINE_PATTERN, content);
        if (line != null) {
            String alias = firstGroup(LANG_PATTERN, line);
            if (alias != null) {
                return Language.getByAlias(alias);
            }
        }
        return Optional.empty();
    }
    
    private String firstGroup(Pattern pattern, String value) {
        String firstGroup = null;
        Matcher matcher = pattern.matcher(value);
        while (matcher.find()) {
            firstGroup = matcher.group(1);
        }
        return firstGroup;
    }
}