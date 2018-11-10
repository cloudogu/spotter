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
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class FirstLineBaseLanguageDetectionStrategyTest {

    private final CapturingLanguageDetectionStrategy strategy = new CapturingLanguageDetectionStrategy();

    @Test
    public void testExtractFirstLine() {
        strategy.detectByContent("#!/bin/bash\nsome more content\none more line");
        assertEquals("#!/bin/bash", strategy.firstLine);
    }

    @Test
    public void testExtractFirstLineWithWindowsLineEndings() {
        strategy.detectByContent("#!/bin/bash\r\nsome more content\r\none more line");
        assertEquals("#!/bin/bash", strategy.firstLine);
    }

    @Test
    public void testExtractFirstLineWithOnlyOneLine() {
        strategy.detectByContent("only one line");
        assertEquals("only one line", strategy.firstLine);
    }

    public static class CapturingLanguageDetectionStrategy extends FirstLineBaseLanguageDetectionStrategy {

        private String firstLine;

        @Override
        protected List<Language> detectByFirstLine(String firstLine) {
            this.firstLine = firstLine;
            return Collections.emptyList();
        }
    }

}
