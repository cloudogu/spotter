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