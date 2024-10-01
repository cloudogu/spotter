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

public class BestEffortMatchingStrategyTest {

  @Test
  public void testSimpleMatch() {
    LanguageDetectionStrategy one = noopDetectionStrategy(Language.JAVASCRIPT);
    BestEffortMatchingStrategy strategy = new BestEffortMatchingStrategy(one);

    Optional<Language> language = strategy.detect(new LanguageDetectionContext("", "/some/path", new byte[0]));
    assertThat(language).contains(Language.JAVASCRIPT);
  }

  @Test
  public void testSimpleBestEffortMatch() {
    LanguageDetectionStrategy one = noopDetectionStrategy(Language.TYPESCRIPT, Language.JAVASCRIPT);
    LanguageDetectionStrategy two = noopDetectionStrategy(Language.JAVASCRIPT);
    BestEffortMatchingStrategy strategy = new BestEffortMatchingStrategy(one, two);

    Optional<Language> language = strategy.detect(new LanguageDetectionContext("", "/some/path", new byte[0]));
    assertThat(language).contains(Language.JAVASCRIPT);
  }

  @Test
  public void testBestEffortMatch() {
    LanguageDetectionStrategy one = noopDetectionStrategy(Language.JAVASCRIPT);
    LanguageDetectionStrategy two = noopDetectionStrategy(Language.TYPESCRIPT, Language.RUBY);
    LanguageDetectionStrategy three = noopDetectionStrategy(Language.TYPESCRIPT);
    BestEffortMatchingStrategy strategy = new BestEffortMatchingStrategy(one, two, three);

    Optional<Language> language = strategy.detect(new LanguageDetectionContext("", "/some/path", new byte[0]));
    assertThat(language).contains(Language.JAVASCRIPT);
  }

  @Test
  public void testNothingMatches() {
    LanguageDetectionStrategy one = noopDetectionStrategy();
    LanguageDetectionStrategy two = noopDetectionStrategy();
    BestEffortMatchingStrategy strategy = new BestEffortMatchingStrategy(one, two);

    Optional<Language> language = strategy.detect(new LanguageDetectionContext("", "/some/path", new byte[0]));
    assertThat(language).isEmpty();
  }

}
