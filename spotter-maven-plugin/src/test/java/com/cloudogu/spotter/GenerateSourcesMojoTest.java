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

import com.google.common.io.Resources;
import org.apache.maven.project.MavenProject;
import org.junit.AssumptionViolatedException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class GenerateSourcesMojoTest {

  @Rule
  public TemporaryFolder temporaryFolder = new TemporaryFolder();

  @Mock
  private MavenProject project;

  @InjectMocks
  private GenerateSourcesMojo mojo;

  private File directory;

  @Before
  @SuppressWarnings("UnstableApiUsage")
  public void setUpMojo() throws IOException {
    directory = temporaryFolder.newFolder();
    mojo.setOutputPath(directory.getAbsolutePath());

    URL url = Resources.getResource("com/cloudogu/spotter/languages.yml");
    mojo.setLanguagesUrl(url.toExternalForm());
    mojo.setPackageName("demo");
    mojo.setLanguagesVersion("v1.0.0");
  }

  @Test
  public void testExecute() throws Exception {
    mojo.execute();

    withGeneratedClass(languageClass -> {
      assertTrue(languageClass.isEnum());
      assertEquals("v1.0.0", languageClass.getDeclaredField("VERSION").get(null));
      assertEquals(5, languageClass.getEnumConstants().length);
    });
  }

  @Test
  public void testExecuteWithPatchFile() throws Exception {
    File patchFile = readPatchFile("com/cloudogu/spotter/prismPatch.yml");
    mojo.setLanguagePatchFiles(Collections.singletonList(patchFile));

    mojo.execute();

    withGeneratedClass(languageClass -> {
      Object java = languageClass.getEnumConstants()[0];
      assertEquals("java", getMode(languageClass, java, "Prism"));
    });
  }

  @SuppressWarnings("unchecked")
  private String getMode(Class<?> languageClass, Object object, String modeName) throws Exception {
    Method getMode = languageClass.getMethod("get" + modeName + "Mode");
    Optional<String> mode = (Optional<String>) getMode.invoke(object);
    return mode.orElseThrow(() -> new AssumptionViolatedException("Mode " + modeName + " not available"));
  }

  @Test
  public void testExecuteWithMultiplePatchFiles() throws Exception {
    File prismPatchFile = readPatchFile("com/cloudogu/spotter/prismPatch.yml");
    File acePatchFile = readPatchFile("com/cloudogu/spotter/acePatch.yml");

    mojo.setLanguagePatchFiles(Arrays.asList(prismPatchFile, acePatchFile));

    mojo.execute();

    withGeneratedClass(languageClass -> {
      Object java = languageClass.getEnumConstants()[0];
      assertEquals("java", getMode(languageClass, java, "Prism"));
      assertEquals("basic", getMode(languageClass, java, "Ace"));
    });
  }

  @SuppressWarnings("UnstableApiUsage")
  private File readPatchFile(String resourceName) throws URISyntaxException {
    URL acePatchUrl = Resources.getResource(resourceName);
    return Paths.get(acePatchUrl.toURI()).toFile();
  }

  private void withGeneratedClass(ThrowingConsumer<Class<?>> consumer) throws Exception {
    File packageFile = new File(directory, "demo");
    File sourceFile = new File(packageFile, "Language.java");
    compile(sourceFile);

    try (URLClassLoader classLoader = createClassLoader(directory)) {
      Class<?> languageClass = classLoader.loadClass("demo.Language");
      consumer.accept(languageClass);
    }
  }

  private void compile(File file) {
    JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
    compiler.run(null, null, null, file.getPath());
  }

  private URLClassLoader createClassLoader(File directory) throws MalformedURLException {
    ClassLoader parent = Thread.currentThread().getContextClassLoader();
    return new URLClassLoader(new URL[]{directory.toURI().toURL()}, parent);
  }

  @FunctionalInterface
  private interface ThrowingConsumer<T> {
    void accept(T value) throws Exception;
  }
}
