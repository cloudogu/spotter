package com.github.sdorra.spotter.internal;

import com.github.sdorra.spotter.Language;

import java.util.Optional;

public interface LanguageDetectionStrategy {

    Optional<Language> detect(String path, byte[] content);

}
