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

import static com.cloudogu.spotter.internal.LanguageTests.assertLang;
import static com.cloudogu.spotter.internal.LanguageTests.assertNotFound;
import static org.assertj.core.api.Assertions.assertThat;

public class ExtensionLanguageDetectionStrategyTest {

  private final ExtensionLanguageDetectionStrategy strategy = new ExtensionLanguageDetectionStrategy();

  @Test
  public void testDetect() {
    assertLang(strategy, Language.PYTHON, "app.py");
    assertLang(strategy, Language.JAVA, "src/main/com/github/sdorra/App.java");
    assertLang(strategy, Language.JSON, "dogu/blueprint.json");
    assertLang(strategy, Language.JSON, "dogu/blueprint-3.1.0-dev.json");
    assertLang(strategy, Language.JAVASCRIPT, "myData/version.test.js");
    assertNotFound(strategy, "scm-manager.png");
  }

  @Test
  public void testDetectWithoutExtension() {
    List<Language> languages = strategy.detectByFilename("Jenkinsfile");
    assertThat(languages).isEmpty();
  }

}
