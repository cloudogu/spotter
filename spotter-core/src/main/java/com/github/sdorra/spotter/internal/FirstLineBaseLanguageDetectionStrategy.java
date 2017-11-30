package com.github.sdorra.spotter.internal;

import com.github.sdorra.spotter.Language;

import java.util.Optional;

public abstract class FirstLineBaseLanguageDetectionStrategy extends StringContentBasedLanguageDetectionStrategy {
    @Override
    protected Optional<Language> detectByContent(String content) {
        int lineSeparator = content.trim().indexOf('\n');
        if (lineSeparator > 0) {
            return detectByFirstLine(content.substring(0, lineSeparator).trim());
        }
        return detectByFirstLine(content);
    }

    protected abstract Optional<Language> detectByFirstLine(String firstLine);
}
