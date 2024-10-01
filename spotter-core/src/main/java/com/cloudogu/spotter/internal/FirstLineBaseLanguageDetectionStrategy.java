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

import java.util.List;

/**
 * Base class for content detection strategies, which are using the first line of the content.
 */
public abstract class FirstLineBaseLanguageDetectionStrategy extends StringContentBasedLanguageDetectionStrategy {
  @Override
  protected List<Language> detectByContent(String content) {
    int lineSeparator = content.trim().indexOf('\n');
    if (lineSeparator > 0) {
      return detectByFirstLine(content.substring(0, lineSeparator).trim());
    }
    return detectByFirstLine(content);
  }

  protected abstract List<Language> detectByFirstLine(String firstLine);
}
