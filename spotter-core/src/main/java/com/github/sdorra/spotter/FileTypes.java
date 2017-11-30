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


package com.github.sdorra.spotter;

import com.github.sdorra.spotter.internal.*;
import org.apache.tika.Tika;

import java.util.Optional;

public class FileTypes {

    private static final byte[] EMPTY_CONTENT = new byte[]{};

    private static final String DEFAULT_CONTENT_TYPE = "application/octet-stream";
    private static final String DEFAULT_CONTENT_TYPE_FOR_LANGUAGE = "text/plain";

    private static final Tika tika = new Tika();

    private static final LanguageDetectionStrategy PATH_BASED_STRATEGY = new FirstMatchLanguageDetectionStrategy(
        new FilenameLanguageDetectionStrategy(),
        new ExtensionLanguageDetectionStrategy()
    );

    private static final LanguageDetectionStrategy PATH_AND_CONTENT_BASED_STRATEGY = new FirstMatchLanguageDetectionStrategy(
        new ViModeLanguageDetectionStrategy(),
        new EmacsModeLanguageDetectionStrategy(),
        new ShebangLanguageDetectionStrategy(),
        new FilenameLanguageDetectionStrategy(),
        new ExtensionLanguageDetectionStrategy()
    );

    private FileTypes(){}

    public static FileType detect(String path) {
        return of(tika.detect(path), PATH_BASED_STRATEGY.detect(path, EMPTY_CONTENT));
    }

    public static FileType detect(String path, byte[] content) {
        return of(tika.detect(content, path), PATH_AND_CONTENT_BASED_STRATEGY.detect(path, content));
    }

    private static FileType of(String contentType, Optional<Language> language) {
        if (contentType.equals(DEFAULT_CONTENT_TYPE) && language.isPresent()) {
            contentType = DEFAULT_CONTENT_TYPE_FOR_LANGUAGE;
        }
        return new FileType(new ContentType(contentType), language);
    }

}
