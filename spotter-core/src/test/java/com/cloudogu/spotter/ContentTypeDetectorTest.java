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

import java.nio.charset.StandardCharsets;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

public class ContentTypeDetectorTest {

  @Test
  public void shouldBoostMarkdown() {
    // without the boost for markdown, the result will be GCC_MACHINE_DESCRIPTION
    ContentTypeDetector detector = ContentTypeDetector.builder()
      .defaultPathAndContentBased()
      .boost(Language.MARKDOWN)
      .bestEffortMatch();

    String content = String.join("\n",
      "% Some content",
      "% Which does not look like markdown"
    );

    ContentType contentType = detector.detect("some.md", content.getBytes(StandardCharsets.UTF_8));
    assertThat(contentType.getLanguage()).contains(Language.MARKDOWN);
  }

  @Test
  public void shouldUseCustom() {
    ContentTypeDetector detector = ContentTypeDetector.builder()
      .custom(ctx -> Collections.singletonList(Language.MAKEFILE))
      .firstMatch();

    ContentType contentType = detector.detect("???");
    assertThat(contentType.getLanguage()).contains(Language.MAKEFILE);
  }

}
