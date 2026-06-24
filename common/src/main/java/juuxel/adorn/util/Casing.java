package juuxel.adorn.util;

import java.util.ArrayList;
import java.util.List;

public final class Casing {
    private final WordSplitter splitter;
    private final WordMerger merger;

    private Casing(WordSplitter splitter, WordMerger merger) {
        this.splitter = splitter;
        this.merger = merger;
    }

    public String convert(String source) {
        return merger.merge(splitter.split(source));
    }

    public static Builder fromCamelCase() {
        return new Builder(Casing::splitCamelCase);
    }

    private static String[] splitCamelCase(String source) {
        List<String> words = new ArrayList<>();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < source.length(); i++) {
            char c = source.charAt(i);

            if ('A' <= c && c <= 'Z') {
                flushStringBuilder(sb, words);
                sb.append((char) (c + 32));
            } else {
                sb.append(c);
            }
        }

        flushStringBuilder(sb, words);
        return words.toArray(String[]::new);
    }

    private static void flushStringBuilder(StringBuilder sb, List<String> words) {
        if (sb.isEmpty()) return;
        words.add(sb.toString());
        sb.setLength(0);
    }

    private static String mergeSnakeCase(String[] words) {
        return String.join("_", words);
    }

    public final static class Builder {
        private final WordSplitter splitter;

        private Builder(WordSplitter splitter) {
            this.splitter = splitter;
        }

        public Casing toSnakeCase() {
            return new Casing(splitter, Casing::mergeSnakeCase);
        }
    }

    private interface WordSplitter {
        String[] split(String source);
    }

    private interface WordMerger {
        String merge(String[] words);
    }
}
