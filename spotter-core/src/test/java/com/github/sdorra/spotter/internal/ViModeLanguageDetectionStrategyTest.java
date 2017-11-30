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

import java.io.IOException;
import java.util.Optional;

import static com.github.sdorra.spotter.internal.LanguageTests.assertLangFromResource;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ViModeLanguageDetectionStrategyTest {

    private final ViModeLanguageDetectionStrategy strategy = new ViModeLanguageDetectionStrategy();

    @Test
    public void testDetect() throws IOException {
        assertLang(Language.PYTHON, "# vi: set ft=python :");
        assertLang(Language.PYTHON, "# vim: set ft=python :");
        assertLang(Language.PYTHON, "# vi: set filetype=python :");
        assertLang(Language.PYTHON, "# vi: set syntax=python :");
        assertLang(Language.PYTHON, "// vi: set syntax=python :");
        assertLang(Language.PYTHON, "/* vi: set syntax=python : */");
        assertLang(Language.PYTHON, "/* vi: set syntax=python : */");

        assertLangFromResource(strategy, Language.RUBY, "com/github/sdorra/spotter/Vagrantfile");
    }

    @Test
    public void testDetectUseLatest() {
        assertLang(Language.PYTHON, "# vi: set ft=ruby :\n# vi: set ft=python :");
    }

    @Test
    public void testDetectWithoutModeline() {
        Optional<Language> optional = strategy.detectByContent("");
        assertFalse(optional.isPresent());
    }

    private void assertLang(Language expected, String content) {
        Optional<Language> optional = strategy.detectByContent(content);
        assertTrue(optional.isPresent());
        assertEquals(expected, optional.get());
    }

}