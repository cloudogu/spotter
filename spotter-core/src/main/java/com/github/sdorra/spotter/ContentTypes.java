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

/**
 * ContentTypes is able to detect the {@link ContentType} of content. ContentTypes uses different strategies to detect
 * the type of a content.
 */
public final class ContentTypes {

    private static final ContentTypeDetector PATH_BASED_DETECTOR = ContentTypeDetector.builder()
        .detection(LanguageDetection.FILENAME, LanguageDetection.EXTENSION)
        .firstMatch();

    private static final ContentTypeDetector PATH_AND_CONTENT_BASED_DETECTOR = ContentTypeDetector.builder()
        .detection(LanguageDetection.VI_MODE, LanguageDetection.EMACS_MODE, LanguageDetection.SHEBANG)
        .detection(LanguageDetection.FILENAME, LanguageDetection.EXTENSION)
        .bestEffortMatch();

    private ContentTypes(){}

    /**
     * Detects the {@link ContentType} of the given path, by only using path based strategies.
     *
     * @param path path of the content
     *
     * @return {@link ContentType} of path
     */
    public static ContentType detect(String path) {
        return PATH_BASED_DETECTOR.detect(path);
    }

    /**
     * Detects the {@link ContentType} of the given path, by using path and content based strategies.
     *
     * @param path path of the content
     * @param contentPrefix first few bytes of the content
     *
     * @return {@link ContentType} of path and content prefix
     */
    public static ContentType detect(String path, byte[] contentPrefix) {
        return PATH_AND_CONTENT_BASED_DETECTOR.detect(path, contentPrefix);
    }
}
