/*
 * The MIT License
 *
 * Copyright 2018 Sebastian Sdorra
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

package com.github.sdorra.spotter.internal;

import com.github.sdorra.spotter.Language;
import com.github.sdorra.spotter.LanguageDetectionContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * BestEffortMatchingStrategy calculates a score for each matched language and returns the one with the highest score.
 * The order of the {@link LanguageDetectionStrategy} are the base for the score calculation, the first strategy creates
 * the highest score and the last the lowest.
 */
public class BestEffortMatchingStrategy implements MatchingStrategy {

    private LanguageDetectionStrategy[] strategies;

    public BestEffortMatchingStrategy(LanguageDetectionStrategy... strategies) {
        this.strategies = strategies;
    }

    @Override
    public Optional<Language> detect(LanguageDetectionContext context) {
        List<Match> matches = new ArrayList<>();

        for (int i=0; i<strategies.length; i++) {
            LanguageDetectionStrategy strategy = strategies[i];

            List<Language> languages = strategy.detect(context);
            context = context.addLanguages(languages);
            for (Language language : languages) {
                addMatch(matches, language, i);
            }
        }

        return Optional.ofNullable( getWithHighestScore(matches) );
    }

    private Language getWithHighestScore(List<Match> matches) {
        Match best = null;
        for ( Match m : matches ) {
            if (best == null || m.score > best.score ) {
                best = m;
            }
        }
        if (best != null) {
            return best.language;
        }
        return null;
    }

    private void addMatch( List<Match> matches, Language language, int index ) {
        int newScore = calculateScore(index);
        Match match = findOrCreateMatch(matches, language);
        match.add(newScore);
    }

    private Match findOrCreateMatch( List<Match> matches, Language language ) {
        for (Match m : matches) {
            if ( m.language == language) {
                return m;
            }
        }
        Match m = new Match(language);
        matches.add(m);
        return m;
    }

    private int calculateScore(int index) {
        return (strategies.length * 100) / (index+1);
    }

    private static class Match {

        private Language language;
        private int score = 0;

        private Match(Language language) {
            this.language = language;
        }

        void add(int score) {
            this.score += score;
        }
    }
}
