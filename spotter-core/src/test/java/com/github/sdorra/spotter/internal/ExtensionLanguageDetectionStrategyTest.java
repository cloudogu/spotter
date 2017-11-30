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

import java.util.Optional;

import static com.github.sdorra.spotter.internal.LanguageTests.assertLang;
import static com.github.sdorra.spotter.internal.LanguageTests.assertNotFound;
import static org.junit.Assert.assertFalse;

public class ExtensionLanguageDetectionStrategyTest {

    private final ExtensionLanguageDetectionStrategy strategy = new ExtensionLanguageDetectionStrategy();

    @Test
    public void testDetect() {
        assertLang(strategy, Language.PYTHON,"app.py");
        assertLang(strategy, Language.JAVA,"src/main/com/github/sdorra/App.java");
        assertNotFound(strategy, "scm-manager.png");
    }

    @Test
    public void testDetectWithoutExtension() {
        Optional<Language> optional = strategy.detectByFilename("Jenkinsfile");
        assertFalse(optional.isPresent());
    }

}