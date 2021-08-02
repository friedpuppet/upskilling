package ua.yelisieiev;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * supports GET request on single location
 * responds with corresponding file content
 * tests:
 * configure
 * start
 * stop
 * send request for existing resource
 * send request for nonexistent resource
 **/
public class FileServerTest {
    FileServer server = new FileServer();

    @DisplayName("Start and stop")
    @Test
    public void test_startStop() throws InterruptedException {
        startServer();
        Thread.sleep(2000);
        stopServer();
    }

    @DisplayName("Start wait 5 mins and stop")
    @Test
    public void test_startWaitStop() throws InterruptedException, IOException {
        try {
            createTestFile("test2.txt", "somesomesome");
            startServer();
            Thread.sleep(300_000);
            stopServer();
        } finally {
            removeTestFile("test2.txt");
        }
    }

    @DisplayName("On running server request existing resource and receive it")
    @Test
    public void test_onRunning_get_receive() {
        try {
            String testData = "Some content";
            createTestFile("./test.txt", testData);
            startServer();
            URL url = new URL("http://localhost:3000/test.txt");

            InputStream inputStream = (InputStream) url.getContent();
            assertEquals(testData, new String(inputStream.readAllBytes()));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            stopServer();
            removeTestFile("./test.txt");
        }
    }

    @DisplayName("On running server request nonexistent resource and receive error")
    @Test
    public void test_onRunning_get_error() throws IOException {
        try {
            startServer();
            URL url = new URL("http://localhost:3000/nofile.txt");
            assertThrows(FileNotFoundException.class, url::getContent);
        } finally {
            stopServer();
        }
    }

    private void startServer() {
        server.setPort(3000);
        server.setContentPath("./");
        server.start();
        while (!server.isStarted()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void stopServer() {
        server.stop();
        while (server.isStarted()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
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
