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


package com.github.sdorra.spotter;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Template;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.*;

@Mojo(name = "generate-sources", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class GenerateSourcesMojo extends AbstractMojo {

    private static final String PACKAGE = "com.github.sdorra.spotter";

    private static final String TEMPLATE = "com/github/sdorra/spotter/language";

    private static final String LANGUAGES_URL = "https://raw.githubusercontent.com/github/linguist/master/lib/linguist/languages.yml";

    @Parameter
    private String packageName = PACKAGE;

    @Parameter(defaultValue = "${project.build.directory}/generated-sources/spotter")
    private String outputPath;

    @Parameter
    private String languagesUrl = LANGUAGES_URL;

    @Parameter(readonly = true, required = true, defaultValue = "${project}")
    private MavenProject project;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info("generate spotter sources " + outputPath);
        project.addCompileSourceRoot(outputPath);

        try {
            Template template = readTemplate(TEMPLATE);

            Languages languages = loadLanguages(languagesUrl);
            Map<String, Object> model = createModel(languages);

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

    private Map<String,Object> createModel(Languages languages) {
        Map<String,Object> model = new LinkedHashMap<>();
        model.put("languages", new ArrayList<>(languages.entrySet()));
        model.put("package", packageName);
        model.put("url", languagesUrl);
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
        handlebars.registerHelper("ek",
            (Helper<String>) (s, options) ->
                    s.toUpperCase(Locale.ENGLISH)
                            .replaceAll("#", "_SHARP")
                            .replaceAll("\\+\\+", "_PLUS")
                            .replaceAll("^[0-9]", "")
                            .replaceAll("[\\s-#'.+()]", "_")

        );
        return handlebars.compile(template);
    }
}
