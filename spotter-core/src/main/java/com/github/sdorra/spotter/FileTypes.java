/**
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

import org.apache.tika.Tika;

import java.nio.file.Paths;
import java.util.Optional;
import com.github.sdorra.spotter.Language;

public class FileTypes {

    private static final String DEFAULT_CONTENT_TYPE = "application/octet-stream";
    private static final String DEFAULT_CONTENT_TYPE_FOR_LANGUAGE = "text/plain";

    private static final Tika tika = new Tika();

    private FileTypes(){}

    public static FileType detect(String path) {
        String contentType = tika.detect(path);
        Optional<Language> language = getLanguageByPath(path);
        if (contentType.equals(DEFAULT_CONTENT_TYPE) && language.isPresent()) {
            contentType = DEFAULT_CONTENT_TYPE_FOR_LANGUAGE;
        }
        return new FileType(new ContentType(contentType), language);
    }

    private static Optional<Language> getLanguageByPath(String path) {
        String filename = getFilenameFromPath(path);
        Optional<Language> byFilename = Language.getByFilename(filename);
        if (!byFilename.isPresent()) {
            Optional<String> extension = getExtensionFromFilename(filename);
            if (extension.isPresent()) {
                return Language.getByExtension(extension.get());
            }
        }
        return byFilename;
    }

    private static String getFilenameFromPath(String path) {
        return Paths.get(path).getFileName().toString();
    }

    private static Optional<String> getExtensionFromFilename(String filename) {
        int index = filename.indexOf('.');
        if (index > 0) {
            return Optional.of(filename.substring(index));
        }
        return Optional.empty();
    }

}
