package ua.yelisieiev;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FileAnalizer {
    private static class Analyzer {
        private final BufferedReader reader;
        private final String word;

        private int wordsCount;
        private List<String> sentences;
        private Set<Character> endSentenceSymbols = Set.of('.',  '!', '?');//new HashSet<Character>();

        private boolean parsed = false;

        public Analyzer(BufferedReader reader, String word) {
            this.reader = reader;
            this.word = word;
        }

        public int getWordsCount() {
            checkParsed();
            return wordsCount;
        }

        public List<String> getSentences() {
            checkParsed();
            return sentences;
        }

        public void analyze() throws IOException {
            checkNotParsed();
            sentences = new ArrayList<>();
            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }

                boolean isASentence = line.length() > 0 && endSentenceSymbols.contains(line.charAt(line.length() -1));
                boolean sentenceAdded = false;

                int currentIndex = 0;
                while (currentIndex != -1 && currentIndex < line.length()) {
                    currentIndex = line.substring(currentIndex).indexOf(word);
                    if (currentIndex != -1) {
                        wordsCount++;
                        currentIndex++;
                        if (isASentence && !sentenceAdded) {
                            sentences.add(line);
                            sentenceAdded = true;
                        }
                    }
                }
            }
//            wordsCount = 3;
//            sentences.add("Though we may be sitting ducks.");
//            sentences.add("Though we may be sitting ducks?");
//            sentences.add("Though we may be sitting ducks!");
            parsed = true;
        }

        private void checkParsed() {
            if (!parsed) {
                throw new RuntimeException("The input wasn't parsed yet");
            }
        }

        private void checkNotParsed() {
            if (parsed) {
                throw new RuntimeException("Input already was analyzed");
            }
        }
    }

    private static void printUsage() {
        System.out.println("Usage:");
        System.out.println("java FileAnalyzer C:/test/story.txt duck");
    }

    public static void main(String[] args) throws IOException {
        if (args.length < 2) {
            printUsage();
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(args[0]))) {
            Analyzer analyzer = new Analyzer(reader, args[1]);
            analyzer.analyze();
            System.out.println(analyzer.getWordsCount());
            for (String sentence : analyzer.getSentences()) {
                System.out.println(sentence);
            }
        }
    }
}
