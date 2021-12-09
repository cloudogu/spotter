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
package com.cloudogu.spotter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Context for language detection.
 */
public class LanguageDetectionContext {

  private final String contentType;
  private final String path;
  private final byte[] content;
  private final Collection<Language> previouslyDetected;

  public LanguageDetectionContext(String contentType, String path, byte[] content) {
    this(contentType, path, content, Collections.emptyList());
  }

  private LanguageDetectionContext(String contentType, String path, byte[] content, Collection<Language> previouslyDetected) {
    this.contentType = contentType;
    this.path = path;
    this.content = content;
    this.previouslyDetected = previouslyDetected;
  }

  /**
   * @return content type detected by content detection
   */
  public String getContentType() {
    return contentType;
  }

  /**
   * @return content path
   */
  public String getPath() {
    return path;
  }

  /**
   * @return first few bytes of content
   */
  public byte[] getContent() {
    return content;
  }

  /**
   * @return languages detected by previous strategies
   */
  public Collection<Language> getPreviouslyDetected() {
    return previouslyDetected;
  }

  /**
   * Creates a copy of this context with the given languages added to the languages of this context.
   *
   * @param additionalLanguages additional languages
   * @return new context with additional languages.
   */
  public LanguageDetectionContext addLanguages(Collection<Language> additionalLanguages) {
    if (additionalLanguages.isEmpty()) {
      return this;
    }
    int overallSize = additionalLanguages.size() + previouslyDetected.size();
    Collection<Language> joinedLanguages = new ArrayList<>(overallSize);
    joinedLanguages.addAll(additionalLanguages);
    joinedLanguages.addAll(this.previouslyDetected);
    return new LanguageDetectionContext(this.contentType, path, content, joinedLanguages);
  }
}
