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

/**
 * Detects the language of the content by using the unix shebang line.
 *
 * @see <a href="https://en.wikipedia.org/wiki/Shebang_(Unix)">Shebang</a>
 */
public class ShebangLanguageDetectionStrategy extends FirstLineBaseLanguageDetectionStrategy {

  @Override
  protected List<Language> detectByFirstLine(String firstLine) {
    if (hasShebang(firstLine)) {
      String interpreter = getInterpreterFromShebangLine(firstLine);
      return Language.getByInterpreter(interpreter);
    }
    return Collections.emptyList();
  }

  private String getInterpreterFromShebangLine(String line) {
    String lineWithoutShebang = removeShebang(line);

    String executable = getExecutableFromLine(lineWithoutShebang);
    if (isEnvExecutable(executable)) {
      executable = getInterpreterFromEnv(executable);
    }

    return splitAndReturnFirstPart(executable, "\\s+");
  }

  private String getInterpreterFromEnv(String executable) {
    return splitAndReturnLastPart(executable, "\\s+");
  }

  private boolean isEnvExecutable(String executable) {
    return executable.startsWith("env");
  }

  private String getExecutableFromLine(String lineWithoutShebang) {
    return splitAndReturnLastPart(lineWithoutShebang, "/");
  }

  private String removeShebang(String firstLine) {
    return firstLine.substring(2);
  }

  private boolean hasShebang(String firstLine) {
    return firstLine.startsWith("#!");
  }

  private String splitAndReturnLastPart(String value, String regex) {
    String[] parts = value.split(regex);
    return parts[parts.length - 1];
  }

  private String splitAndReturnFirstPart(String value, String regex) {
    String[] parts = value.split(regex);
    return parts[0];
  }
}
