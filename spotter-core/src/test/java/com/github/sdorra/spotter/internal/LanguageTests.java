package com.github.sdorra.spotter.internal;

import com.github.sdorra.spotter.Language;
import com.github.sdorra.spotter.internal.LanguageDetectionStrategy;
import com.google.common.io.Resources;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

final class LanguageTests {

    private LanguageTests() {
    }

    static Optional<Language> langFromResource(LanguageDetectionStrategy strategy, String resource) throws IOException {
        URL url = Resources.getResource(resource);
        byte[] content = Resources.toByteArray(url);

        return strategy.detect(resource, content);
    }

    static void assertLangFromResource(LanguageDetectionStrategy strategy, Language expected, String resource) throws IOException {
        Optional<Language> optional = langFromResource(strategy, resource);
        assertTrue(optional.isPresent());
        assertEquals(expected, optional.get());
    }

    static void assertLang(LanguageDetectionStrategy strategy, Language expected, String path) {
        Optional<Language> optional = strategy.detect(path, new byte[]{});
        assertTrue(optional.isPresent());
        assertEquals(expected, optional.get());
    }

    static void assertNotFound(LanguageDetectionStrategy strategy, String path) {
        Optional<Language> optional = strategy.detect(path, new byte[]{});
        assertFalse(optional.isPresent());
    }
}
