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

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class ContentTypeTest {

  @Test
  public void testGetPrimary() {
    ContentType contentType = new ContentType("text/plain");
    assertEquals("text", contentType.getPrimary());
  }

  @Test
  public void testGetSecondary() {
    ContentType contentType = new ContentType("text/plain");
    assertEquals("plain", contentType.getSecondary());
  }

  @Test
  public void testToString() {
    ContentType contentType = new ContentType("text/plain");
    assertEquals("text/plain", contentType.toString());
  }

  @Test
  public void testGetRaw() {
    ContentType contentType = new ContentType("text/plain");
    assertEquals("text/plain", contentType.getRaw());
  }

  @Test
  public void testEqualsAndHashCode() {
    assertEqualInclusiveHashCode(new ContentType("text/plain"), new ContentType("text/plain"));
    assertNotEqualInclusiveHashCode(new ContentType("text/plain"), new ContentType("image/png"));
  }

  private void assertEqualInclusiveHashCode(ContentType left, ContentType right) {
    assertEquals(left, right);
    assertEquals(left.hashCode(), right.hashCode());
  }

  private void assertNotEqualInclusiveHashCode(ContentType left, ContentType right) {
    assertNotEquals(left, right);
    assertNotEquals(left.hashCode(), right.hashCode());
  }

  @Test
  public void testEqualsWithLanguage() {
    assertEqualInclusiveHashCode(
      new ContentType("text/plain", Optional.of(Language.JAVASCRIPT)),
      new ContentType("text/plain", Optional.of(Language.JAVASCRIPT))
    );
    assertNotEqualInclusiveHashCode(
      new ContentType("text/plain", Optional.of(Language.JAVASCRIPT)),
      new ContentType("text/plain", Optional.of(Language.TYPESCRIPT))
    );
  }

  @Test
  public void testIsTextWithTextBasedContentType() {
    ContentType contentType = new ContentType("text/plain");
    assertTrue(contentType.isText());
  }

  @Test
  public void testIsTextWithTextLanguage() {
    ContentType contentType = new ContentType("application/javascript", Optional.of(Language.JAVASCRIPT));
    assertTrue(contentType.isText());
  }

  @Test
  public void testIsTextWithNonText() {
    ContentType contentType = new ContentType("application/pdf");
    assertFalse(contentType.isText());
  }

  @Test
  public void testGradleReturnsPrismMode() {
    assertEquals("groovy", Language.GRADLE.getPrismMode().get());
  }

  @Test
  public void testRustReturnsPrismMode() {
    assertEquals("rust", Language.RUST.getPrismMode().get());
  }
}
