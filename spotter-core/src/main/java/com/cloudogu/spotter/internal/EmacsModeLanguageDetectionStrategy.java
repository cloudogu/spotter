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

import java.util.regex.Pattern;

/**
 * Detects languages of content, by using emacs mode line of the content.
 */
public class EmacsModeLanguageDetectionStrategy extends RegexBasedLanguageDetectionStrategy {

  private static final Pattern PATTERN_LINE = Pattern.compile(".*-\\*-\\s*(.+?)\\s*-\\*-.*(?m:$)");

  private static final Pattern PATTERN_LANG = Pattern.compile(".*(?i:mode)\\s*:\\s*([^\\s;]+)\\s*;*.*");

  /**
   * Creates new {@link EmacsModeLanguageDetectionStrategy}.
   */
  public EmacsModeLanguageDetectionStrategy() {
    super(PATTERN_LINE, PATTERN_LANG);
  }
}
