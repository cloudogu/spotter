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

}
