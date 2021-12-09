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
}
