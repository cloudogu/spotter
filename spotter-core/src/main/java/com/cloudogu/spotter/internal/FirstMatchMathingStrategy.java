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

import java.util.List;
import java.util.Optional;

/**
 * The {@link FirstMatchMathingStrategy} iterates over multiple {@link LanguageDetectionStrategy} and returns
 * the first result of a matching strategy.
 */
public class FirstMatchMathingStrategy implements MatchingStrategy {

  private LanguageDetectionStrategy[] strategies;

  /**
   * Creates a new {@link FirstMatchMathingStrategy}.
   *
   * @param strategies array of strategies
   */
  public FirstMatchMathingStrategy(LanguageDetectionStrategy... strategies) {
    this.strategies = strategies;
  }

  @Override
  public Optional<Language> detect(LanguageDetectionContext context) {
    for (LanguageDetectionStrategy strategy : strategies) {
      List<Language> lang = strategy.detect(context);
      if (!lang.isEmpty()) {
        return Optional.of(lang.get(0));
      }
    }
    return Optional.empty();
  }

}
