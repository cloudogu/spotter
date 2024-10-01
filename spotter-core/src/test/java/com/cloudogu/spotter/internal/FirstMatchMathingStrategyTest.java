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
import com.cloudogu.spotter.LanguageDetectionStrategy;
import org.junit.Test;

import java.util.Optional;

import static com.cloudogu.spotter.internal.LanguageTests.noopDetectionStrategy;
import static org.assertj.core.api.Assertions.assertThat;

public class FirstMatchMathingStrategyTest {

  @Test
  public void testSimpleMatch() {
    LanguageDetectionStrategy languageDetectionStrategy = noopDetectionStrategy(Language.JAVA);
    FirstMatchMathingStrategy mathingStrategy = new FirstMatchMathingStrategy(languageDetectionStrategy);

    Optional<Language> language = mathingStrategy.detect(new LanguageDetectionContext("", "/some/file", new byte[0]));
    assertThat(language).contains(Language.JAVA);
  }

  @Test
  public void testSecondMatches() {
    LanguageDetectionStrategy one = noopDetectionStrategy();
    LanguageDetectionStrategy two = noopDetectionStrategy(Language.JAVA);
    FirstMatchMathingStrategy mathingStrategy = new FirstMatchMathingStrategy(one, two);

    Optional<Language> language = mathingStrategy.detect(new LanguageDetectionContext("", "/some/file", new byte[0]));
    assertThat(language).contains(Language.JAVA);
  }

  @Test
  public void testNoMatch() {
    LanguageDetectionStrategy one = noopDetectionStrategy();
    LanguageDetectionStrategy two = noopDetectionStrategy();
    FirstMatchMathingStrategy mathingStrategy = new FirstMatchMathingStrategy(one, two);

    Optional<Language> language = mathingStrategy.detect(new LanguageDetectionContext("", "/some/file", new byte[0]));
    assertThat(language).isEmpty();
  }

  @Test
  public void testOneMatchesMultiple() {
    LanguageDetectionStrategy one = noopDetectionStrategy(Language.JAVASCRIPT, Language.TYPESCRIPT);
    FirstMatchMathingStrategy mathingStrategy = new FirstMatchMathingStrategy(one);

    Optional<Language> language = mathingStrategy.detect(new LanguageDetectionContext("", "/some/file", new byte[0]));
    assertThat(language).contains(Language.JAVASCRIPT);
  }

}
