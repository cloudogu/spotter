package com.github.sdorra.spotter.internal;

import com.github.sdorra.spotter.Language;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class LanguageTest {

    @Test
    public void testAceMode() {
        assertThat(Language.JAVA.getAceMode()).contains("java");
        assertThat(Language.GO.getAceMode()).contains("golang");
    }

    @Test
    public void testAceModeTextShouldBeEmpty() {
        assertThat(Language.ABNF.getAceMode()).isEmpty();
    }

    @Test
    public void testCodemirrorMode() {
        assertThat(Language.JAVA.getCodemirrorMode()).contains("clike");
        assertThat(Language.GO.getCodemirrorMode()).contains("go");
    }
}
