package ua.yelisieiev;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

class RequestHandlerTest {

    @Test
    @DisplayName("On correct request serve correct response")
    void test_onCorrectRequest_serveCorrectResponse() throws IOException {
        try {
            String testData = "Some test data\nAnother line of some data.";
            createTestFile("./test.txt", testData);

            String inputData = "GET /test.txt HTTP/1.1\r\n" +
                    "Host: localhost\r\n" +
                    "Accept: */*";
            ByteArrayInputStream byteIn = new ByteArrayInputStream(inputData.getBytes());
            BufferedInputStream inputStream = new BufferedInputStream(byteIn);

            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            BufferedOutputStream outputStream = new BufferedOutputStream(byteOut);

            RequestHandler requestHandler = new RequestHandler(inputStream, outputStream, "./");
            requestHandler.handle();

            String testHeader = "HTTP/1.1 200 OK\r\n" +
                    "Content-type: application/octet-stream\r\n" +
                    "Content-length: 41\r\n" +
                    "\r\n";
            assertEquals(testHeader + testData, byteOut.toString());
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