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

package com.github.sdorra.spotter.internal;

import com.github.sdorra.spotter.Language;
import com.github.sdorra.spotter.LanguageDetectionContext;
import com.github.sdorra.spotter.LanguageDetectionStrategy;
import org.junit.Test;

import java.util.Optional;

import static com.github.sdorra.spotter.internal.LanguageTests.noopDetectionStrategy;
import static org.assertj.core.api.Assertions.assertThat;

public class BestEffortMatchingStrategyTest {

    @Test
    public void testSimpleMatch() {
        LanguageDetectionStrategy one = noopDetectionStrategy(Language.JAVASCRIPT);
        BestEffortMatchingStrategy strategy = new BestEffortMatchingStrategy(one);

        Optional<Language> language = strategy.detect(new LanguageDetectionContext("", "/some/path", new byte[0]));
        assertThat(language).contains(Language.JAVASCRIPT);
    }

    @Test
    public void testSimpleBestEffortMatch() {
        LanguageDetectionStrategy one = noopDetectionStrategy(Language.TYPESCRIPT, Language.JAVASCRIPT);
        LanguageDetectionStrategy two = noopDetectionStrategy(Language.JAVASCRIPT);
        BestEffortMatchingStrategy strategy = new BestEffortMatchingStrategy(one, two);

        Optional<Language> language = strategy.detect(new LanguageDetectionContext("", "/some/path", new byte[0]));
        assertThat(language).contains(Language.JAVASCRIPT);
    }

    @Test
    public void testBestEffortMatch() {
        LanguageDetectionStrategy one = noopDetectionStrategy(Language.JAVASCRIPT);
        LanguageDetectionStrategy two = noopDetectionStrategy(Language.TYPESCRIPT, Language.RUBY);
        LanguageDetectionStrategy three = noopDetectionStrategy(Language.TYPESCRIPT);
        BestEffortMatchingStrategy strategy = new BestEffortMatchingStrategy(one, two, three);

        Optional<Language> language = strategy.detect(new LanguageDetectionContext("", "/some/path", new byte[0]));
        assertThat(language).contains(Language.JAVASCRIPT);
    }

    @Test
    public void testNothingMatches() {
        LanguageDetectionStrategy one = noopDetectionStrategy();
        LanguageDetectionStrategy two = noopDetectionStrategy();
        BestEffortMatchingStrategy strategy = new BestEffortMatchingStrategy(one, two);

        Optional<Language> language = strategy.detect(new LanguageDetectionContext("", "/some/path", new byte[0]));
        assertThat(language).isEmpty();
    }

}
