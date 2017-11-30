/*
 * The MIT License
 *
 * Copyright 2017 Sebastian Sdorra
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

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
