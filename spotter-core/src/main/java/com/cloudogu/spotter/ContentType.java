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

import java.util.Objects;
import java.util.Optional;

/**
 * Type of content.
 */
public final class ContentType {

  private final String raw;
  private final Optional<Language> language;

  /**
   * Creates a new ContentType from raw and without language.
   *
   * @param raw content type in raw format
   */
  public ContentType(String raw) {
    this(raw, Optional.empty());
  }

  /**
   * Creates a new ContentType from raw and language.
   *
   * @param raw      content type in raw format
   * @param language optional language of the content
   */
  public ContentType(String raw, Optional<Language> language) {
    this.raw = raw;
    this.language = language;
  }

  /**
   * Returns the primary part of the content type (e.g.: text of text/plain).
   *
   * @return primary content type part
   */
  public String getPrimary() {
    return raw.split("/")[0];
  }

  /**
   * Returns the secondary part of the content type (e.g.: plain of text/plain).
   *
   * @return secondary content type part
   */
  public String getSecondary() {
    String[] parts = raw.split("/");
    return parts[parts.length - 1];
  }

  /**
   * Returns {@code true} if the content type is text based.
   *
   * @return {@code true} if the content type is text based
   */
  public boolean isText() {
    return "text".equals(getPrimary()) || language.isPresent();
  }

  /**
   * Returns an optional language of the content.
   *
   * @return optional content type
   */
  public Optional<Language> getLanguage() {
    return language;
  }

  /**
   * Returns the raw presentation of the content type (e.g.: text/plain).
   *
   * @return raw presentation
   */
  public String getRaw() {
    return raw;
  }

  @Override
  public String toString() {
    return raw;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ContentType that = (ContentType) o;
    return Objects.equals(raw, that.raw)
      && Objects.equals(language, that.language);
  }

  @Override
  public int hashCode() {
    return Objects.hash(raw, language);
  }
}
