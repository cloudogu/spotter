package com.cloudogu.spotter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Context for language detection.
 */
public class LanguageDetectionContext {

  private final String contentType;
  private final String path;
  private final byte[] content;
  private final Collection<Language> previouslyDetected;

  public LanguageDetectionContext(String contentType, String path, byte[] content) {
    this(contentType, path, content, Collections.emptyList());
  }

  private LanguageDetectionContext(String contentType, String path, byte[] content, Collection<Language> previouslyDetected) {
    this.contentType = contentType;
    this.path = path;
    this.content = content;
    this.previouslyDetected = previouslyDetected;
  }

  /**
   * @return content type detected by content detection
   */
  public String getContentType() {
    return contentType;
  }

  /**
   * @return content path
   */
  public String getPath() {
    return path;
  }

  /**
   * @return first few bytes of content
   */
  public byte[] getContent() {
    return content;
  }

  /**
   * @return languages detected by previous strategies
   */
  public Collection<Language> getPreviouslyDetected() {
    return previouslyDetected;
  }

  /**
   * Creates a copy of this context with the given languages added to the languages of this context.
   *
   * @param additionalLanguages additional languages
   * @return new context with additional languages.
   */
  public LanguageDetectionContext addLanguages(Collection<Language> additionalLanguages) {
    if (additionalLanguages.isEmpty()) {
      return this;
    }
    int overallSize = additionalLanguages.size() + previouslyDetected.size();
    Collection<Language> joinedLanguages = new ArrayList<>(overallSize);
    joinedLanguages.addAll(additionalLanguages);
    joinedLanguages.addAll(this.previouslyDetected);
    return new LanguageDetectionContext(this.contentType, path, content, joinedLanguages);
  }
}
