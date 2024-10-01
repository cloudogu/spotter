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

package com.cloudogu.spotter;

import java.util.List;

/**
 * Strategy for detecting possible {@link Language} of content.
 */
public interface LanguageDetectionStrategy {

  /**
   * Detects possible languages of a path and or the content.
   *
   * @param context Context for the detection
   * @return list of matching languages
   */
  List<Language> detect(LanguageDetectionContext context);
}
