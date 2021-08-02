package ua.yelisieiev;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.*;

/**
 * 1: Написать программу FileAnalyzer, которая в консоли принимает 2 параметра:
 * 1) путь к файлу
 * 2) слово
 * Usage:
 * java FileAnalyzer C:/test/story.txt duck
 * <p>
 * Выводит:
 * 1) Кол-во вхождений искомого слова в файле
 * 2) Все предложения содержащие искомое слово(предложение заканчивается символами ".", "?", "!"),
 * каждое преждложение с новой строки.
 *
 * tests be like:
 * call with wrong params - see usage
 * with custom file count words
 * with custom file show sentences
 **/
public class FileAnalyzerTest {
    private final ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
    private final PrintStream out = new PrintStream(bytesOut);


    private final String testFileName = "sitting_ducks.txt";

    @BeforeEach
    private void replaceOut() {
        System.setOut(out);
    }

    private void createDucksFile() throws IOException {
        String testFileContent = """
                Shut down.
                Fall in.
                Weak mind.
                Thick skin.

                And we squawk like silly birds.
                Making all of our noises!
                Though we may be sitting ducks.
                We've got all of our poisons?
                All of our poisons in a row!
                In a row.

                Zone out.
                Light dim.
                Lost sight.
                Can't swim.

                And we squawk like silly birds.
                Making all of our noises.
                Though we may be sitting ducks
                Though we may be sitting ducks?
                We've got all of our poisons.
                All of our poisons in a row.
                A row, in a row.
                In a row, a row.
                In a row!
                And we squawk like silly birds.
                Making all of our noises.
                Though we may be sitting ducks!
                We've go all of our poisons.
                In a row!
                A row, in a row.
                In a row, a row.
                In a row?
                In a row.
                A row, a row, a row.
                In a row.""";
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(testFileName));
        bufferedOutputStream.write(testFileContent.getBytes());
        bufferedOutputStream.close();
    }

    private BufferedReader getOutReader() {
        return new BufferedReader(new InputStreamReader(
                new ByteArrayInputStream(bytesOut.toByteArray())));
    }

    @DisplayName("On wrong parameters - show usage")
    @Test
    public void test_withWrongParams_showUsage() throws IOException {
        String[] args = {};
        FileAnalizer.main(args);
        String USAGE_STRING = "Usage:\njava FileAnalyzer C:/test/story.txt duck\n";
        assertEquals(USAGE_STRING, bytesOut.toString());
    }

    @DisplayName("On custom file - count words")
    @Test
    public void test_withCustomFile_countWords() throws IOException {
        createDucksFile();
        String[] args = {testFileName, "ducks"};
        FileAnalizer.main(args);
        BufferedReader reader = getOutReader();
        assertEquals("4", reader.readLine());
    }

    @DisplayName("On custom file - show sentences")
    @Test
    public void test_withCustomFile_showSentences() throws IOException {
        createDucksFile();
        String[] args = {testFileName, "ducks"};
        FileAnalizer.main(args);
        BufferedReader reader = getOutReader();
        reader.readLine();
        assertEquals("Though we may be sitting ducks.", reader.readLine());
        assertEquals("Though we may be sitting ducks?", reader.readLine());
        assertEquals("Though we may be sitting ducks!", reader.readLine());
    }
}
