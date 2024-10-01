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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


public class ShebangLanguageDetectionStrategyTest {

  private final ShebangLanguageDetectionStrategy strategy = new ShebangLanguageDetectionStrategy();

  @Test
  public void testDetect() {
    List<Language> languages = strategy.detectByFirstLine("#!/bin/bash");
    assertThat(languages).contains(Language.SHELL);
  }

  @Test
  public void testDetectWithoutSheband() {
    List<Language> languages = strategy.detectByFirstLine("some text, which is not a shebang");
    assertThat(languages).isEmpty();
  }

  @Test
  public void testDetecWithEnv() {
    List<Language> languages = strategy.detectByFirstLine("#!/usr/bin/env node");
    assertThat(languages).contains(Language.JAVASCRIPT);
  }

  @Test
  public void testDetectWithParameter() {
    List<Language> languages = strategy.detectByFirstLine("#!/usr/bin/make -f");
    assertThat(languages).contains(Language.MAKEFILE);
  }

}
