package com.github.sdorra.spotter.internal;

import com.github.sdorra.spotter.Language;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexBasedLanguageDetectionStrategy extends StringContentBasedLanguageDetectionStrategy {

    private final Pattern linePattern;
    private final Pattern langPattern;

    public RegexBasedLanguageDetectionStrategy(Pattern linePattern, Pattern langPattern) {
        this.linePattern = linePattern;
        this.langPattern = langPattern;
    }

    @Override
    protected Optional<Language> detectByContent(String content) {
        String line = firstGroup(linePattern, content);
        if (line != null) {
            String alias = firstGroup(langPattern, line);
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
