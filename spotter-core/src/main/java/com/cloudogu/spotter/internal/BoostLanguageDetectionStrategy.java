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
import java.util.Collection;
import java.util.List;

/**
 * Boosts already matched languages.
 *
 * @since 3.0.0
 */
public class BoostLanguageDetectionStrategy implements LanguageDetectionStrategy {

  private final Language[] languages;

  public BoostLanguageDetectionStrategy(Language... languages) {
    this.languages = languages;
  }

  @Override
  public List<Language> detect(LanguageDetectionContext context) {
    List<Language> boost = new ArrayList<>();
    Collection<Language> previouslyDetected = context.getPreviouslyDetected();
    for (Language language : languages) {
      if (previouslyDetected.contains(language)) {
        boost.add(language);
      }
    }
    return boost;
  }
}
