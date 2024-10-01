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
