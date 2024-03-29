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
