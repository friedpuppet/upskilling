package ua.yelisieiev;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

class ResponseWriterTest {

    @Test
    @DisplayName("With correct response send it's content to output writer")
    void writeResponse() throws IOException {
        String testData = "Some test data\nAnother line of some data.";

        ByteArrayInputStream byteIn = new ByteArrayInputStream(testData.getBytes());
        BufferedInputStream inputStream = new BufferedInputStream(byteIn);
        Response response = new Response(HttpMethod.GET, HttpStatus.OK, inputStream);

        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        BufferedOutputStream outputStream = new BufferedOutputStream(byteOut);

        ResponseWriter.writeResponse(outputStream, response);

        String testHeader = "HTTP/1.1 200 OK\r\n" +
                "Content-type: application/octet-stream\r\n" +
                "Content-length: 41\r\n" +
                "\r\n";
        assertEquals(testHeader + testData, byteOut.toString());
    }

    @Test
    @DisplayName("With error response send it's content to output writer")
    void writeErrorResponse() throws IOException {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        BufferedOutputStream outputStream = new BufferedOutputStream(byteOut);
        ResponseWriter.writeErrorResponse(outputStream, HttpStatus.NOT_FOUND);
        assertEquals("HTTP/1.1 404 Not found\r\n\r\n", byteOut.toString());
    }
}