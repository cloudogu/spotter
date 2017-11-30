package com.github.sdorra.spotter.internal;

import com.github.sdorra.spotter.Language;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.*;

public class ShebangLanguageDetectionStrategyTest {

    private final ShebangLanguageDetectionStrategy strategy = new ShebangLanguageDetectionStrategy();

    @Test
    public void testDetect() {
        Optional<Language> optional = strategy.detectByFirstLine("#!/bin/bash");
        assertTrue(optional.isPresent());
        assertEquals(Language.SHELL, optional.get());
    }

    @Test
    public void testDetectWithoutSheband() {
        Optional<Language> optional = strategy.detectByFirstLine("some text, which is not a shebang");
        assertFalse(optional.isPresent());
    }

    @Test
    public void testDetecWithEnv() {
        Optional<Language> optional = strategy.detectByFirstLine("#!/usr/bin/env node");
        assertTrue(optional.isPresent());
        assertEquals(Language.JAVASCRIPT, optional.get());
    }

}