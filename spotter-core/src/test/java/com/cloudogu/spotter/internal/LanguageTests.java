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
import com.google.common.collect.Lists;
import com.google.common.io.Resources;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

final class LanguageTests {

  private LanguageTests() {
  }

  static List<Language> langFromResource(LanguageDetectionStrategy strategy, String resource) throws IOException {
    URL url = Resources.getResource(resource);
    byte[] content = Resources.toByteArray(url);

    return strategy.detect(new LanguageDetectionContext("", resource, content));
  }

  static void assertLangFromResource(LanguageDetectionStrategy strategy, Language expected, String resource) throws IOException {
    List<Language> languages = langFromResource(strategy, resource);
    assertThat(languages).contains(expected);
  }

  static void assertLang(LanguageDetectionStrategy strategy, Language expected, String path) {
    List<Language> languages = strategy.detect(new LanguageDetectionContext("", path, new byte[]{}));
    assertThat(languages).contains(expected);
  }

  static void assertNotFound(LanguageDetectionStrategy strategy, String path) {
    List<Language> languages = strategy.detect(new LanguageDetectionContext("", path, new byte[]{}));
    assertThat(languages).isEmpty();
  }

  static LanguageDetectionStrategy noopDetectionStrategy(Language... languages) {
    List<Language> list = Lists.newArrayList(languages);
    return context -> list;
  }
}
