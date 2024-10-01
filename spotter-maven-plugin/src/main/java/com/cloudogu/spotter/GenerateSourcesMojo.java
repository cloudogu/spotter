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
import org.apache.maven.shared.model.fileset.FileSet;
import org.apache.maven.shared.model.fileset.util.FileSetManager;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

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

  @Parameter
  private FileSet languagePatchFiles;

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

  @VisibleForTesting
  void setLanguagePatchFiles(FileSet languagePatchFiles) {
    this.languagePatchFiles = languagePatchFiles;
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

    List<File> patchFiles = getPatchFiles();
    if (!patchFiles.isEmpty()) {
      JsonMerger merger = new JsonMerger(mapper);
      JsonMerger.MergeStage mergeStage = merger.fromJson(mapper.readTree(url));
      for (File f : patchFiles) {
        getLog().info("apply patch " + f);
        mergeStage.mergeWithJson(mapper.readTree(f));
      }
      return mergeStage.toObject(Languages.class);
    }

    return mapper.readValue(url, Languages.class);
  }

  private List<File> getPatchFiles() {
    if (languagePatchFiles != null) {
      FileSetManager fileSetManager = new FileSetManager();
      String directory = languagePatchFiles.getDirectory();
      String[] included = fileSetManager.getIncludedFiles(languagePatchFiles);
      return Arrays.stream(included).map(fileName -> new File(directory, fileName)).collect(Collectors.toList());
    }
    return Collections.emptyList();
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
