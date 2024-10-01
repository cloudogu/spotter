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

import java.nio.file.Paths;
import java.util.List;

/**
 * Base class for filename based detection strategies.
 */
public abstract class FilenameBasedLanguageDetectionStrategy implements LanguageDetectionStrategy {

  @Override
  public List<Language> detect(LanguageDetectionContext context) {
    String filename = Paths.get(context.getPath()).getFileName().toString();
    return detectByFilename(filename);
  }

  protected abstract List<Language> detectByFilename(String filename);
}
