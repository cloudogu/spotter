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
import com.github.sdorra.spotter.internal.ContentTypeBoostLanguageDetectionStrategy;
import com.github.sdorra.spotter.internal.EmacsModeLanguageDetectionStrategy;
import com.github.sdorra.spotter.internal.ExtensionLanguageDetectionStrategy;
import com.github.sdorra.spotter.internal.FilenameLanguageDetectionStrategy;
import com.github.sdorra.spotter.internal.FirstMatchMathingStrategy;
import com.github.sdorra.spotter.internal.MatchingStrategy;
import com.github.sdorra.spotter.internal.ShebangLanguageDetectionStrategy;
import com.github.sdorra.spotter.internal.ViModeLanguageDetectionStrategy;
import org.apache.tika.Tika;

import java.util.ArrayList;
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

        private final List<LanguageDetectionStrategy> detectionStrategies = new ArrayList<>();

        /**
         * Adds a strategy which uses the filename to detect the language of the content.
         *
         * @return {@code this}
         * @since 3.0.0
         */
        public Builder filename() {
            detectionStrategies.add(new FilenameLanguageDetectionStrategy());
            return this;
        }

        /**
         * Adds a strategy which uses the file extension to detect language of the content.
         *
         * @return {@code this}
         * @since 3.0.0
         */
        public Builder extension() {
            detectionStrategies.add(new ExtensionLanguageDetectionStrategy());
            return this;
        }

        /**
         * Adds a strategy which uses the shebang to detect language of the content.
         * This detection strategy requires content.
         *
         * @see <a href="https://en.wikipedia.org/wiki/Shebang_(Unix)">emacs modeline magic</a>
         * @return {@code this}
         * @since 3.0.0
         */
        public Builder shebang() {
            detectionStrategies.add(new ShebangLanguageDetectionStrategy());
            return this;
        }

        /**
         * Adds a strategy which uses the emacs mode line to detect language of the content.
         * This detection strategy requires content.
         *
         * @see <a href="https://www.gnu.org/software/emacs/manual/html_node/emacs/Specifying-File-Variables.html">emacs modeline magic</a>
         * @return {@code this}
         * @since 3.0.0
         */
        public Builder emacsMode() {
            detectionStrategies.add(new EmacsModeLanguageDetectionStrategy());
            return this;
        }

        /**
         * Adds a strategy which uses the vi mode line to detect language of the content.
         * This detection strategy requires content.
         *
         * @see <a href="http://vim.wikia.com/wiki/Modeline_magic">vim modeline magic</a>
         * @return {@code this}
         * @since 3.0.0
         */
        public Builder viMode() {
            detectionStrategies.add(new ViModeLanguageDetectionStrategy());
            return this;
        }

        /**
         * Adds a strategy which boost previously detected languages, which are using the same content type as declared
         * in the context.
         *
         * @return {@code this}
         * @since 3.0.0
         */
        public Builder contentTypeBoost() {
            detectionStrategies.add(new ContentTypeBoostLanguageDetectionStrategy());
            return this;
        }

        /**
         * Adds a custom {@link LanguageDetectionStrategy}.
         *
         * @param strategy custom strategy
         * @return {@code this}
         * @since 3.0.0
         */
        public Builder custom(LanguageDetectionStrategy strategy) {
            detectionStrategies.add(strategy);
            return this;
        }

        /**
         * Returns {@link ContentTypeDetector} which uses a best effort strategy for matching.
         * Note the order in which the strategies was added matters.
         *
         * @return best effort content type detector
         */
        public ContentTypeDetector bestEffortMatch() {
            return new ContentTypeDetector(new BestEffortMatchingStrategy(detectionAsArray()));
        }

        /**
         * Return {@link ContentTypeDetector} which uses first match strategy for matching.
         * Note the order in which the strategies was added matters.
         *
         * @return first match content type detector.
         */
        public ContentTypeDetector firstMatch() {
            return new ContentTypeDetector(new FirstMatchMathingStrategy(detectionAsArray()));
        }

        private LanguageDetectionStrategy[] detectionAsArray() {
            return detectionStrategies.toArray(new LanguageDetectionStrategy[0]);
        }
    }
}
