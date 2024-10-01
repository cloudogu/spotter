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
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class LanguageTest {

  @Test
  public void testAceMode() {
    assertThat(Language.JAVA.getAceMode()).contains("java");
    assertThat(Language.GO.getAceMode()).contains("golang");
  }

  @Test
  public void testAceModeTextShouldBeEmpty() {
    assertThat(Language.ABNF.getAceMode()).isEmpty();
  }

  @Test
  public void testCodemirrorMode() {
    assertThat(Language.JAVA.getCodemirrorMode()).contains("clike");
    assertThat(Language.GO.getCodemirrorMode()).contains("go");
  }
}
