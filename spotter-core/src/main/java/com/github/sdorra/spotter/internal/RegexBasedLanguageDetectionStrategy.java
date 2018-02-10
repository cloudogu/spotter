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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Base class for regex based detection strategies.
 */
public class RegexBasedLanguageDetectionStrategy extends StringContentBasedLanguageDetectionStrategy {

    private final Pattern linePattern;
    private final Pattern langPattern;

    /**
     * Creates a new regex base language strategy.
     *
     * @param linePattern pattern to match the line
     * @param langPattern pattern to match the language within the line
     */
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
