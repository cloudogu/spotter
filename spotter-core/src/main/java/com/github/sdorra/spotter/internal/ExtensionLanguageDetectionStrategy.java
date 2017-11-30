package com.github.sdorra.spotter.internal;

import com.github.sdorra.spotter.Language;

import java.util.Optional;

public class ExtensionLanguageDetectionStrategy extends FilenameBasedLanguageDetectionStrategy {

    @Override
    protected Optional<Language> detectByFilename(String filename) {
        int index = filename.indexOf('.');
        if (index > 0) {
            String extension = filename.substring(index);
            return Language.getByExtension(extension);
        }
        return Optional.empty();
    }
}
