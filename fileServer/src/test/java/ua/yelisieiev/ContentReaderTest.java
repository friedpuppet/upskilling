package ua.yelisieiev;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

class ContentReaderTest {

    @Test
    @DisplayName("For existing file get reader")
    void test_forExistingFile_getReader() throws IOException {
        try {
            String testData = "Some test data\nAnother line of some data.";
            createTestFile("./test.txt", testData);

            ContentReader contentReader = new ContentReader("./");
            BufferedInputStream inputStream = contentReader.getInputStream("/test.txt");
            byte[] buff = new byte[1024];
            int count = inputStream.read(buff);
            assertEquals(testData, new String(buff, 0, count));
            inputStream.close();
        } finally {
            removeTestFile("./test.txt");
        }
    }

    private void removeTestFile(String name) {
        File testFile = new File(name);
        testFile.delete();
    }

    private void createTestFile(String name, String content) throws IOException {
        File testFile = new File(name);
        testFile.delete();
        FileOutputStream fileOutputStream = new FileOutputStream(testFile);
        fileOutputStream.write(content.getBytes());
        fileOutputStream.close();
    }
}