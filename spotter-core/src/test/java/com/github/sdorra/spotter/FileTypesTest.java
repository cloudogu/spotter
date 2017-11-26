/**
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


package com.github.sdorra.spotter;

import com.github.sdorra.spotter.Language;
import org.junit.Test;

import static org.junit.Assert.*;

public class FileTypesTest {

    @Test
    public void testDetectWithJavaFile() {
        FileType type = FileTypes.detect("com/github/sdorra/Language.java");
        assertEquals("text/x-java-source", type.getContentType().getRaw());
        assertEquals(Language.JAVA, type.getLanguage().get());
    }

    @Test
    public void testDetectWithImage() {
        FileType type = FileTypes.detect("com/github/sdorra/scm-manager.png");
        assertEquals("image/png", type.getContentType().getRaw());
        assertFalse(type.isText());
        assertFalse(type.getLanguage().isPresent());
    }

    @Test
    public void testDetectWithDockerfile() {
        FileType type = FileTypes.detect("Dockerfile");
        assertEquals("text/plain", type.getContentType().getRaw());
        assertTrue(type.isText());
        assertEquals(Language.DOCKERFILE, type.getLanguage().get());
    }



}