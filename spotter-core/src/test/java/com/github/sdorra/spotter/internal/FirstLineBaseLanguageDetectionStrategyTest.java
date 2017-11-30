package com.github.sdorra.spotter.internal;

import com.github.sdorra.spotter.Language;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.*;

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
        protected Optional<Language> detectByFirstLine(String firstLine) {
            this.firstLine = firstLine;
            return Optional.empty();
        }
    }

}