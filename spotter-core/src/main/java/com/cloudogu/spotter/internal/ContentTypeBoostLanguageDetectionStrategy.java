package com.cloudogu.spotter.internal;

import com.cloudogu.spotter.Language;
import com.cloudogu.spotter.LanguageDetectionContext;
import com.cloudogu.spotter.LanguageDetectionStrategy;

import java.util.List;
import java.util.Locale;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ContentTypeBoostLanguageDetectionStrategy implements LanguageDetectionStrategy {

  @Override
  public List<Language> detect(LanguageDetectionContext context) {
    MediaTypeMatcher matchesContentType = new MediaTypeMatcher(context.getContentType());
    return context
      .getPreviouslyDetected()
      .stream()
      .filter(matchesContentType)
      .collect(Collectors.toList());
  }

  private static class MediaTypeMatcher implements Predicate<Language> {
    private final String lowerCaseContentType;

    private MediaTypeMatcher(String contentType) {
      this.lowerCaseContentType = contentType.toLowerCase(Locale.ENGLISH);
    }

    @Override
    public boolean test(Language language) {
      return lowerCaseContentType.contains(language.getName().toLowerCase(Locale.ENGLISH));
    }
  }
}
