package com.github.sdorra.spotter.internal;

import com.github.sdorra.spotter.Language;

import java.util.Optional;

public class FilenameLanguageDetectionStrategy extends FilenameBasedLanguageDetectionStrategy {

    @Override
    protected Optional<Language> detectByFilename(String filename) {
        return Language.getByFilename(filename);
    }
}
