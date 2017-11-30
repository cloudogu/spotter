package com.github.sdorra.spotter.internal;

import com.github.sdorra.spotter.Language;

import java.util.Optional;

public class FirstMatchLanguageDetectionStrategy implements LanguageDetectionStrategy {

    private LanguageDetectionStrategy[] strategies;

    public FirstMatchLanguageDetectionStrategy(LanguageDetectionStrategy... strategies) {
        this.strategies = strategies;
    }

    @Override
    public Optional<Language> detect(String path, byte[] content) {
        for (LanguageDetectionStrategy strategy : strategies) {
            Optional<Language> lang = strategy.detect(path, content);
            if (lang.isPresent()) {
                return lang;
            }
        }
        return Optional.empty();
    }
}
