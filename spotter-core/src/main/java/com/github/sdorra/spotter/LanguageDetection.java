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

import com.github.sdorra.spotter.internal.EmacsModeLanguageDetectionStrategy;
import com.github.sdorra.spotter.internal.ExtensionLanguageDetectionStrategy;
import com.github.sdorra.spotter.internal.FilenameLanguageDetectionStrategy;
import com.github.sdorra.spotter.internal.LanguageDetectionStrategy;
import com.github.sdorra.spotter.internal.ShebangLanguageDetectionStrategy;
import com.github.sdorra.spotter.internal.ViModeLanguageDetectionStrategy;

/**
 * LanguageDetection describes an strategy for detecting the programming language of content.
 * @since 1.2.0
 */
public enum LanguageDetection {

    /**
     * Uses the vi mode line to detect language of the content.
     * This detection strategy requires content.
     *
     * @see <a href="http://vim.wikia.com/wiki/Modeline_magic">vim modeline magic</a>
     */
    VI_MODE(new ViModeLanguageDetectionStrategy()),

    /**
     * Uses the emacs mode line to detect language of the content.
     * This detection strategy requires content.
     *
     * @see <a href="https://www.gnu.org/software/emacs/manual/html_node/emacs/Specifying-File-Variables.html">emacs modeline magic</a>
     */
    EMACS_MODE(new EmacsModeLanguageDetectionStrategy()),

    /**
     * Uses the shebang to detect language of the content.
     * This detection strategy requires content.
     *
     * @see <a href="https://en.wikipedia.org/wiki/Shebang_(Unix)">emacs modeline magic</a>
     */
    SHEBANG(new ShebangLanguageDetectionStrategy()),

    /**
     * Uses the filename to detect the language of the content.
     */
    FILENAME(new FilenameLanguageDetectionStrategy()),

    /**
     * Uses the file extension to detect language of the content.
     */
    EXTENSION(new ExtensionLanguageDetectionStrategy());

    private LanguageDetectionStrategy strategy;

    LanguageDetection(LanguageDetectionStrategy strategy) {
        this.strategy = strategy;
    }

    LanguageDetectionStrategy getStrategy() {
        return strategy;
    }
}
