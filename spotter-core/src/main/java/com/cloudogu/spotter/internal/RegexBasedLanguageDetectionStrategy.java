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

import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Base class for regex based detection strategies.
 */
public class RegexBasedLanguageDetectionStrategy extends StringContentBasedLanguageDetectionStrategy {

  private final Pattern linePattern;
  private final Pattern langPattern;

  /**
   * Creates a new regex base language strategy.
   *
   * @param linePattern pattern to match the line
   * @param langPattern pattern to match the language within the line
   */
  public RegexBasedLanguageDetectionStrategy(Pattern linePattern, Pattern langPattern) {
    this.linePattern = linePattern;
    this.langPattern = langPattern;
  }

  @Override
  protected List<Language> detectByContent(String content) {
    String line = firstGroup(linePattern, content);
    if (line != null) {
      String alias = firstGroup(langPattern, line);
      if (alias != null) {
        return Language.getByAlias(alias);
      }
    }
    return Collections.emptyList();
  }

  private String firstGroup(Pattern pattern, String value) {
    String firstGroup = null;
    Matcher matcher = pattern.matcher(value);
    while (matcher.find()) {
      firstGroup = matcher.group(1);
    }
    return firstGroup;
  }
}
