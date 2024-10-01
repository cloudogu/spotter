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

/**
 * ContentTypes is able to detect the {@link ContentType} of content. ContentTypes uses different strategies to detect
 * the type of a content.
 */
public final class ContentTypes {

  private static final ContentTypeDetector PATH_BASED_DETECTOR = ContentTypeDetector.builder()
    .defaultPathBased().bestEffortMatch();

  private static final ContentTypeDetector PATH_AND_CONTENT_BASED_DETECTOR = ContentTypeDetector.builder()
    .defaultPathAndContentBased().bestEffortMatch();

  private ContentTypes() {
  }

  /**
   * Detects the {@link ContentType} of the given path, by only using path based strategies.
   *
   * @param path path of the content
   * @return {@link ContentType} of path
   */
  public static ContentType detect(String path) {
    return PATH_BASED_DETECTOR.detect(path);
  }

  /**
   * Detects the {@link ContentType} of the given path, by using path and content based strategies.
   *
   * @param path          path of the content
   * @param contentPrefix first few bytes of the content
   * @return {@link ContentType} of path and content prefix
   */
  public static ContentType detect(String path, byte[] contentPrefix) {
    return PATH_AND_CONTENT_BASED_DETECTOR.detect(path, contentPrefix);
  }
}
