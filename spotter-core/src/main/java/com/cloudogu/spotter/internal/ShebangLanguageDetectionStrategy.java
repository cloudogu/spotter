/*
 * MIT License
 *
 * Copyright (c) 2021, Cloudogu GmbH
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
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
