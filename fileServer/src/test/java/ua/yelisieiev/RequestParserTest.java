package ua.yelisieiev;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

class RequestParserTest {
    @Test
    @DisplayName("With known request for an input check if parser produces the equivalent")
    public void test() throws IOException {
        Request expectedRequest = new Request();
        expectedRequest.setUri("/test.txt");
        expectedRequest.setMethod(HttpMethod.GET);
        expectedRequest.setHeader("Host", "localhost");
        expectedRequest.setHeader("Accept", "*/*");

        RequestParser requestParser = new RequestParser();
        String testRequest = """
                GET /test.txt HTTP/1.1
                Host: localhost
                Accept: */*
                """;
        ByteArrayInputStream byteReader = new ByteArrayInputStream(testRequest.getBytes());
        BufferedReader reader = new BufferedReader(new InputStreamReader(byteReader));
        Request request = requestParser.parseRequest(reader);

        System.out.println(expectedRequest);
        System.out.println(request);

        assertEquals(expectedRequest, request);
    }

}