/*
 * The MIT License
 *
 * Copyright 2020 Sebastian Sdorra
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

package com.cloudogu.spotter.internal;

import com.cloudogu.spotter.Language;
import com.cloudogu.spotter.LanguageDetectionContext;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


public class BoostLanguageDetectionStrategyTest {

  @Test
  public void shouldBoostMatchedLanguage() {
    LanguageDetectionContext ctx = createContext(Language.MARKDOWN);
    assertThat(detect(ctx, Language.MARKDOWN)).contains(Language.MARKDOWN);
  }

  @Test
  public void shouldNotBoostNotMatchedLanguages() {
    LanguageDetectionContext ctx = createContext(Language.JAVASCRIPT, Language.JAVA);
    assertThat(detect(ctx)).isEmpty();
  }

  @Test
  public void shouldBoostMatchedLanguages() {
    LanguageDetectionContext ctx = createContext(Language.JAVASCRIPT, Language.JAVA, Language.MARKDOWN);
    assertThat(detect(ctx, Language.JAVA, Language.JAVASCRIPT)).containsOnly(Language.JAVASCRIPT, Language.JAVA);
  }

  private List<Language> detect(LanguageDetectionContext ctx, Language... boost) {
    return new BoostLanguageDetectionStrategy(boost).detect(ctx);
  }

  private LanguageDetectionContext createContext(Language... previouslyDetectedLanguages) {
    return new LanguageDetectionContext(
      "text/plain", "README.md", new byte[0]
    ).addLanguages(Arrays.asList(previouslyDetectedLanguages));
  }

}
