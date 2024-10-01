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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * BestEffortMatchingStrategy calculates a score for each matched language and returns the one with the highest score.
 * The order of the {@link LanguageDetectionStrategy} are the base for the score calculation, the first strategy creates
 * the highest score and the last the lowest.
 */
public class BestEffortMatchingStrategy implements MatchingStrategy {

  private LanguageDetectionStrategy[] strategies;

  public BestEffortMatchingStrategy(LanguageDetectionStrategy... strategies) {
    this.strategies = strategies;
  }

  @Override
  public Optional<Language> detect(LanguageDetectionContext context) {
    List<Match> matches = new ArrayList<>();

    for (int i = 0; i < strategies.length; i++) {
      LanguageDetectionStrategy strategy = strategies[i];

      List<Language> languages = strategy.detect(context);
      context = context.addLanguages(languages);
      for (Language language : languages) {
        addMatch(matches, language, i);
      }
    }

    return Optional.ofNullable(getWithHighestScore(matches));
  }

  private Language getWithHighestScore(List<Match> matches) {
    Match best = null;
    for (Match m : matches) {
      if (best == null || m.score > best.score) {
        best = m;
      }
    }
    if (best != null) {
      return best.language;
    }
    return null;
  }

  private void addMatch(List<Match> matches, Language language, int index) {
    int newScore = calculateScore(index);
    Match match = findOrCreateMatch(matches, language);
    match.add(newScore);
  }

  private Match findOrCreateMatch(List<Match> matches, Language language) {
    for (Match m : matches) {
      if (m.language == language) {
        return m;
      }
    }
    Match m = new Match(language);
    matches.add(m);
    return m;
  }

  private int calculateScore(int index) {
    return (strategies.length * 100) / (index + 1);
  }

  private static class Match {

    private Language language;
    private int score = 0;

    private Match(Language language) {
      this.language = language;
    }

    void add(int score) {
      this.score += score;
    }
  }
}
