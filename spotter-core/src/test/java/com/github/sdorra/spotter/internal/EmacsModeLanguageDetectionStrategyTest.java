package com.github.sdorra.spotter.internal;

import com.github.sdorra.spotter.Language;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.*;

public class EmacsModeLanguageDetectionStrategyTest {

    private EmacsModeLanguageDetectionStrategy strategy = new EmacsModeLanguageDetectionStrategy();

    @Test
    public void testDetect() {
        Optional<Language> optional = strategy.detectByContent("# -*- mode: ruby -*-");
        assertTrue(optional.isPresent());
        assertEquals(Language.RUBY, optional.get());
    }

}