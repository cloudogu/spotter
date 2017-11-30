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