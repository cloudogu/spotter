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

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Language {

  private String type;
  private String color;
  private List<String> aliases;
  private List<String> extensions;
  private List<String> filenames;
  private List<String> interpreters;
  @JsonProperty("ace_mode")
  private String aceMode;
  @JsonProperty("codemirror_mode")
  private String codemirrorMode;
  @JsonProperty("prism_mode")
  private String prismMode;

  public String getType() {
    return type;
  }

  public String getColor() {
    return color;
  }

  public List<String> getAliases() {
    return aliases;
  }

  public List<String> getExtensions() {
    return extensions;
  }

  public List<String> getFilenames() {
    return filenames;
  }

  public List<String> getInterpreters() {
    return interpreters;
  }

  public String getAceMode() {
    return aceMode;
  }

  public String getCodemirrorMode() {
    return codemirrorMode;
  }

  public String getPrismMode() {
    return prismMode;
  }
}
