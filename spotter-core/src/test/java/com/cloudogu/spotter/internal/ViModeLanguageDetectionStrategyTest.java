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

import java.io.IOException;
import java.util.List;

import static com.cloudogu.spotter.internal.LanguageTests.assertLangFromResource;
import static org.assertj.core.api.Assertions.assertThat;

public class ViModeLanguageDetectionStrategyTest {

  private final ViModeLanguageDetectionStrategy strategy = new ViModeLanguageDetectionStrategy();

  @Test
  public void testDetect() throws IOException {
    assertLang(Language.PYTHON, "# vi: set ft=python :");
    assertLang(Language.PYTHON, "# vim: set ft=python :");
    assertLang(Language.PYTHON, "# vi: set filetype=python :");
    assertLang(Language.PYTHON, "# vi: set syntax=python :");
    assertLang(Language.PYTHON, "// vi: set syntax=python :");
    assertLang(Language.PYTHON, "/* vi: set syntax=python : */");
    assertLang(Language.PYTHON, "/* vi: set syntax=python : */");

    assertLangFromResource(strategy, Language.RUBY, "com/cloudogu/spotter/Vagrantfile");
  }

  @Test
  public void testDetectUseLatest() {
    assertLang(Language.PYTHON, "# vi: set ft=ruby :\n# vi: set ft=python :");
  }

  @Test
  public void testDetectWithoutModeline() {
    List<Language> languages = strategy.detectByContent("");
    assertThat(languages).isEmpty();
  }

  private void assertLang(Language expected, String content) {
    List<Language> languages = strategy.detectByContent(content);
    assertThat(languages).contains(expected);
  }

}
