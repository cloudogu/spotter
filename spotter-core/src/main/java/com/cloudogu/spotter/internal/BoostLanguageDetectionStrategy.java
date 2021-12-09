/*
 * The MIT License
 *
 * Copyright 2020 Sebastian Sdorra
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

package com.cloudogu.spotter.internal;

import com.cloudogu.spotter.Language;
import com.cloudogu.spotter.LanguageDetectionContext;
import com.cloudogu.spotter.LanguageDetectionStrategy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Boosts already matched languages.
 *
 * @since 3.0.0
 */
public class BoostLanguageDetectionStrategy implements LanguageDetectionStrategy {

  private final Language[] languages;

  public BoostLanguageDetectionStrategy(Language... languages) {
    this.languages = languages;
  }

  @Override
  public List<Language> detect(LanguageDetectionContext context) {
    List<Language> boost = new ArrayList<>();
    Collection<Language> previouslyDetected = context.getPreviouslyDetected();
    for (Language language : languages) {
      if (previouslyDetected.contains(language)) {
        boost.add(language);
      }
    }
    return boost;
  }
}
