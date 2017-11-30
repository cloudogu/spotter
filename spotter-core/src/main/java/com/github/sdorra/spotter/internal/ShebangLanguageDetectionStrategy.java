package com.github.sdorra.spotter.internal;

import com.github.sdorra.spotter.Language;

import java.util.Optional;

public class ShebangLanguageDetectionStrategy extends FirstLineBaseLanguageDetectionStrategy {
    @Override
    protected Optional<Language> detectByFirstLine(String firstLine) {
        if (hasShebang(firstLine)) {
            String interpreter = getInterpreterFromShebangLine(firstLine);
            return Language.getByInterpreter(interpreter);
        }
        return Optional.empty();
    }

    private String getInterpreterFromShebangLine(String line) {
        String lineWithoutShebang = removeShebang(line);

        String executable = getExecutableFromPath(lineWithoutShebang);
        if (isEnvExecutable(executable)) {
            return getInterpreterFromEnv(executable);
        }

        return executable;
    }

    private String getInterpreterFromEnv(String executable) {
        return splitAndReturnLastPart(executable, "\\s+");
    }

    private boolean isEnvExecutable(String executable) {
        return executable.startsWith("env");
    }

    private String getExecutableFromPath(String lineWithoutShebang) {
        return splitAndReturnLastPart(lineWithoutShebang, "/");
    }

    private String removeShebang(String firstLine) {
        return firstLine.substring(2);
    }

    private boolean hasShebang(String firstLine) {
        return firstLine.startsWith("#!");
    }

    private String splitAndReturnLastPart(String value, String regex) {
        String[] parts = value.split(regex);
        return parts[ parts.length - 1 ];
    }
}
