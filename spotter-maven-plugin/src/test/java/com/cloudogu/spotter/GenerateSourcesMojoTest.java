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

import com.google.common.io.Resources;
import org.apache.maven.project.MavenProject;
import org.apache.maven.shared.model.fileset.FileSet;
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
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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

      Method getByName = languageClass.getMethod("getByName", String.class);
      Optional<?> java = (Optional<?>) getByName.invoke(null, "Java");
      assertNotNull(java);
      assertTrue(java.isPresent());
    });
  }

  @Test
  public void testExecuteWithPatchFile() throws Exception {
    FileSet patchFile = readPatchFiles("prismPatch.yml");
    mojo.setLanguagePatchFiles(patchFile);

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
    FileSet fs = readPatchFiles("prismPatch.yml", "acePatch.yml");
    mojo.setLanguagePatchFiles(fs);

    mojo.execute();

    withGeneratedClass(languageClass -> {
      Object java = languageClass.getEnumConstants()[0];
      assertEquals("java", getMode(languageClass, java, "Prism"));
      assertEquals("basic", getMode(languageClass, java, "Ace"));
    });
  }

  @SuppressWarnings("UnstableApiUsage")
  private FileSet readPatchFiles(String... resourceNames) throws URISyntaxException {
    URL url = Resources.getResource("com/cloudogu/spotter");
    File directory = Paths.get(url.toURI()).toFile();

    FileSet fs = new FileSet();
    fs.setDirectory(directory.getAbsolutePath());
    fs.setIncludes(Arrays.asList(resourceNames));
    return fs;
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
