/*
 * The MIT License
 *
 * Copyright 2018 Sebastian Sdorra
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

import com.github.sdorra.spotter.internal.BestEffortMatchingStrategy;
import com.github.sdorra.spotter.internal.FirstMatchMathingStrategy;
import com.github.sdorra.spotter.internal.MatchingStrategy;
import org.apache.tika.Tika;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * The ContentTypeDetector can be used to create a individual content type and language detector. This class should only
 * if you are know what you are doing, if not the {@link ContentTypes} class should match most of the use cases.
 *
 * @since 1.2.0
 */
public class ContentTypeDetector {

    private static final byte[] EMPTY_CONTENT = new byte[]{};

    private static final String DEFAULT_CONTENT_TYPE = "application/octet-stream";
    private static final String DEFAULT_CONTENT_TYPE_FOR_LANGUAGE = "text/plain";

    private static final Tika tika = new Tika();

    private final MatchingStrategy matchingStrategy;

    private ContentTypeDetector(MatchingStrategy matchingStrategy) {
        this.matchingStrategy = matchingStrategy;
    }

    /**
     * Detects the {@link ContentType} of the given path.
     *
     * @param path path of the content
     *
     * @return {@link ContentType} of path
     */
    public ContentType detect(String path) {
        String contentType = tika.detect(path);
        return of(contentType, matchingStrategy.detect(new LanguageDetectionContext(contentType, path, EMPTY_CONTENT)));
    }

    /**
     * Detects the {@link ContentType} of the given path and or content.
     *
     * @param path path of the content
     * @param contentPrefix first few bytes of the content
     *
     * @return {@link ContentType} of path and content prefix
     */
    public ContentType detect(String path, byte[] contentPrefix) {
        String contentType = tika.detect(contentPrefix, path);
        return of(contentType, matchingStrategy.detect(new LanguageDetectionContext(contentType, path, contentPrefix)));
    }

    private ContentType of(String contentType, Optional<Language> language) {
        if (contentType.equals(DEFAULT_CONTENT_TYPE) && language.isPresent()) {
            contentType = DEFAULT_CONTENT_TYPE_FOR_LANGUAGE;
        }
        return new ContentType(contentType, language);
    }

    /**
     * Returns a builder to create a individual content type detector.
     *
     * @return builder for content type detector
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder for {@link ContentTypeDetector}.
     */
    public static class Builder {

        private final List<LanguageDetection> detectionStrategies = new ArrayList<>();

        /**
         * Add language detections to the list of used strategies for determining the language of the content.
         *
         * @param detection language detection strategy
         * @param detections more detection strategies
         *
         * @return {@code this}
         */
        public Builder detection(LanguageDetection detection, LanguageDetection... detections) {
            detectionStrategies.add(detection);
            detectionStrategies.addAll(Arrays.asList(detections));
            return this;
        }

        /**
         * Returns {@link ContentTypeDetector} which uses a best effort strategy for matching.
         *
         * @return best effort content type detector
         */
        public ContentTypeDetector bestEffortMatch() {
            return new ContentTypeDetector(new BestEffortMatchingStrategy(detectionAsArray()));
        }

        /**
         * Return {@link ContentTypeDetector} which uses first match strategy for matching.
         *
         * @return first match content type detector.
         */
        public ContentTypeDetector firstMatch() {
            return new ContentTypeDetector(new FirstMatchMathingStrategy(detectionAsArray()));
        }

        private LanguageDetectionStrategy[] detectionAsArray() {
            return detectionStrategies.stream()
                .map(LanguageDetection::getStrategy)
                .toArray(LanguageDetectionStrategy[]::new);
        }
    }
}
