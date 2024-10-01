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
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class FirstLineBaseLanguageDetectionStrategyTest {

  private final CapturingLanguageDetectionStrategy strategy = new CapturingLanguageDetectionStrategy();

  @Test
  public void testExtractFirstLine() {
    strategy.detectByContent("#!/bin/bash\nsome more content\none more line");
    assertEquals("#!/bin/bash", strategy.firstLine);
  }

  @Test
  public void testExtractFirstLineWithWindowsLineEndings() {
    strategy.detectByContent("#!/bin/bash\r\nsome more content\r\none more line");
    assertEquals("#!/bin/bash", strategy.firstLine);
  }

  @Test
  public void testExtractFirstLineWithOnlyOneLine() {
    strategy.detectByContent("only one line");
    assertEquals("only one line", strategy.firstLine);
  }

  public static class CapturingLanguageDetectionStrategy extends FirstLineBaseLanguageDetectionStrategy {

    private String firstLine;

    @Override
    protected List<Language> detectByFirstLine(String firstLine) {
      this.firstLine = firstLine;
      return Collections.emptyList();
    }
  }

}
