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

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Template;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Strings;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Mojo for generating language enum from githubs linguist languages.yml file.
 */
@Mojo(name = "generate-sources", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class GenerateSourcesMojo extends AbstractMojo {

  private static final String PACKAGE = "com.cloudogu.spotter";

  private static final String TEMPLATE = "com/cloudogu/spotter/language";

  private static final String LANGUAGES_URL_TEMPLATE = "https://raw.githubusercontent.com/github/linguist/%s/lib/linguist/languages.yml";

  @Parameter
  private String packageName = PACKAGE;

  @Parameter(defaultValue = "${project.build.directory}/generated-sources/spotter")
  private String outputPath;

  @Parameter
  private String languagesUrl;

  @Parameter(defaultValue = "master")
  private String languagesVersion;

  @Parameter(readonly = true, required = true, defaultValue = "${project}")
  private MavenProject project;

  @VisibleForTesting
  void setOutputPath(String outputPath) {
    this.outputPath = outputPath;
  }

  @VisibleForTesting
  void setLanguagesUrl(String languagesUrl) {
    this.languagesUrl = languagesUrl;
  }

  @VisibleForTesting
  void setPackageName(String packageName) {
    this.packageName = packageName;
  }

  @VisibleForTesting
  void setLanguagesVersion(String languagesVersion) {
    this.languagesVersion = languagesVersion;
  }

  @Override
  public void execute() throws MojoExecutionException {
    getLog().info("generate spotter sources " + outputPath);
    project.addCompileSourceRoot(outputPath);

    try {
      Template template = readTemplate(TEMPLATE);

      String url = createLanguagesUrl();
      getLog().info("download languages file from " + url);

      Languages languages = loadLanguages(url);
      Map<String, Object> model = createModel(languages, url);

      String packagePath = packageName.replaceAll("\\.", "/");
      File directory = new File(outputPath, packagePath);
      if (!directory.exists() && !directory.mkdirs()) {
        throw new MojoExecutionException("could not create output directory " + directory.getPath());
      }

      getLog().info("output directory " + directory.getPath());

      try (FileWriter writer = new FileWriter(new File(directory, "Language.java"))) {
        template.apply(model, writer);
      }
    } catch (IOException ex) {
      throw new MojoExecutionException("failed to generate sources", ex);
    }
  }

  private String createLanguagesUrl() {
    if (Strings.isNullOrEmpty(languagesUrl)) {
      return String.format(LANGUAGES_URL_TEMPLATE, languagesVersion);
    }
    return languagesUrl;
  }

  private Map<String, Object> createModel(Languages languages, String url) {
    Map<String, Object> model = new LinkedHashMap<>();
    model.put("languages", new ArrayList<>(languages.entrySet()));
    model.put("package", packageName);
    model.put("url", url);
    model.put("version", languagesVersion);
    model.put("date", ZonedDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
    return model;
  }

  private Languages loadLanguages(String languagesUrl) throws IOException {
    URL url = new URL(languagesUrl);
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    return mapper.readValue(url, Languages.class);
  }

  private Template readTemplate(String template) throws IOException {
    Handlebars handlebars = new Handlebars();
    handlebars.registerHelper("mode", (Helper<String>) (s, options) -> {
      // treat text as null, because it is always the default for highlighting
      if (Strings.isNullOrEmpty(s) || "text".equalsIgnoreCase(s)) {
        return "null";
      }
      return "\"" + s + "\"";
    });
    handlebars.registerHelper("ek",
      (Helper<String>) (s, options) ->
        s.toUpperCase(Locale.ENGLISH)
          .replace("#", "_SHARP")
          .replaceAll("\\+\\+", "_PLUS")
          .replaceAll("\\*", "_STAR")
          .replaceAll("^([0-9])", "_$1")
          .replaceAll("[\\s-#'.+()]", "_")

    );
    return handlebars.compile(template);
  }
}
