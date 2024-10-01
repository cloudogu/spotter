/*
 * Copyright (c) 2020 - present Cloudogu GmbH
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Affero General Public License as published by the Free
 * Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see https://www.gnu.org/licenses/.
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
