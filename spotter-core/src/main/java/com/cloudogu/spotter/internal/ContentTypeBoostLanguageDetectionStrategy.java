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
import java.util.Locale;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ContentTypeBoostLanguageDetectionStrategy implements LanguageDetectionStrategy {

  @Override
  public List<Language> detect(LanguageDetectionContext context) {
    MediaTypeMatcher matchesContentType = new MediaTypeMatcher(context.getContentType());
    return context
      .getPreviouslyDetected()
      .stream()
      .filter(matchesContentType)
      .collect(Collectors.toList());
  }

  private static class MediaTypeMatcher implements Predicate<Language> {
    private final String lowerCaseContentType;

    private MediaTypeMatcher(String contentType) {
      this.lowerCaseContentType = contentType.toLowerCase(Locale.ENGLISH);
    }

    @Override
    public boolean test(Language language) {
      return lowerCaseContentType.contains(language.getName().toLowerCase(Locale.ENGLISH));
    }
  }
}
