/*
 * The MIT License
 *
 * Copyright 2017 Sebastian Sdorra
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
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
