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

import org.junit.Test;

import java.io.UnsupportedEncodingException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ContentTypesTest {

  @Test
  public void testDetectWithJavaFile() {
    ContentType type = ContentTypes.detect("com/github/sdorra/Language.java");
    assertEquals("text/x-java-source", type.getRaw());
    assertEquals(Language.JAVA, type.getLanguage().get());
  }

  @Test
  public void testDetectWithImage() {
    ContentType type = ContentTypes.detect("com/github/sdorra/scm-manager.png");
    assertEquals("image/png", type.getRaw());
    assertFalse(type.isText());
    assertFalse(type.getLanguage().isPresent());
  }

  @Test
  public void testDetectWithMarkdown() {
    ContentType type = ContentTypes.detect("com/github/sdorra/scm-manager.md");
    assertEquals("text/x-web-markdown", type.getRaw());
    assertTrue(type.isText());
    assertTrue(type.getLanguage().isPresent());
    assertEquals(Language.MARKDOWN, type.getLanguage().get());
  }

  @Test
  public void testDetectWithDockerfile() {
    ContentType type = ContentTypes.detect("Dockerfile");
    assertEquals("text/plain", type.getRaw());
    assertTrue(type.isText());
    assertEquals(Language.DOCKERFILE, type.getLanguage().get());
  }

  @Test
  public void testDetectWithViModeLine() throws UnsupportedEncodingException {
    ContentType type = ContentTypes.detect("startup", "# vim: set syntax=ruby :".getBytes("UTF-8"));
    assertEquals("text/plain", type.getRaw());
    assertTrue(type.isText());
    assertEquals(Language.RUBY, type.getLanguage().get());
  }

  @Test
  public void testDetectWithEmacsModeLine() throws UnsupportedEncodingException {
    ContentType type = ContentTypes.detect("startup", "# -*- mode: ruby -*-".getBytes("UTF-8"));
    assertEquals("text/plain", type.getRaw());
    assertTrue(type.isText());
    assertEquals(Language.RUBY, type.getLanguage().get());
  }

  @Test
  public void testDetectWithSheBang() throws UnsupportedEncodingException {
    ContentType type = ContentTypes.detect("startup", "#!/bin/bash".getBytes("UTF-8"));
    assertEquals("application/x-sh", type.getRaw());
    assertTrue(type.isText());
    assertEquals(Language.SHELL, type.getLanguage().get());
  }

  @Test
  public void testDetectWithSheBangAndParameters() throws UnsupportedEncodingException {
    ContentType type = ContentTypes.detect("rules", "#!/usr/bin/make -f".getBytes("UTF-8"));
    assertEquals("application/x-sh", type.getRaw());
    assertTrue(type.isText());
    assertEquals(Language.MAKEFILE, type.getLanguage().get());
  }

  @Test
  public void testDetectMultipleMatches() throws UnsupportedEncodingException {
    // shebang matches javascript and typescript, but the js extension should match only javascript
    ContentType type = ContentTypes.detect("/index.js", "#!/usr/bin/env node".getBytes("UTF-8"));
    assertThat(type.getLanguage()).contains(Language.JAVASCRIPT);
  }
}
