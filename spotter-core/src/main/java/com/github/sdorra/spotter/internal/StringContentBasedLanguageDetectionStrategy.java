package com.github.sdorra.spotter.internal;

import com.github.sdorra.spotter.Language;

import java.util.Optional;

public abstract class StringContentBasedLanguageDetectionStrategy implements LanguageDetectionStrategy {
    @Override
    public Optional<Language> detect(String path, byte[] content) {
        return detectByContent(new String(content));
    }

    protected abstract Optional<Language> detectByContent(String content);
}
