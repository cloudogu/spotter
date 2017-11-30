package com.github.sdorra.spotter.internal;

import com.github.sdorra.spotter.Language;

import java.nio.file.Paths;
import java.util.Optional;

public abstract class FilenameBasedLanguageDetectionStrategy implements LanguageDetectionStrategy {

    @Override
    public Optional<Language> detect(String path, byte[] content) {
        String filename = Paths.get(path).getFileName().toString();
        return detectByFilename(filename);
    }

    protected abstract Optional<Language> detectByFilename(String filename);
}
