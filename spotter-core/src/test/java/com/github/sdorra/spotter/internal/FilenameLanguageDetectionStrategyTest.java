package com.github.sdorra.spotter.internal;

import com.github.sdorra.spotter.Language;
import org.junit.Test;

import static com.github.sdorra.spotter.internal.LanguageTests.assertLang;
import static com.github.sdorra.spotter.internal.LanguageTests.assertNotFound;

public class FilenameLanguageDetectionStrategyTest {

    private final FilenameLanguageDetectionStrategy strategy = new FilenameLanguageDetectionStrategy();

    @Test
    public void testDetect() {
        assertLang(strategy, Language.DOCKERFILE,"docker/Dockerfile");
        assertLang(strategy, Language.RUBY,"Vagrantfile");
        assertNotFound(strategy, "scm-manager.png");
    }
}
