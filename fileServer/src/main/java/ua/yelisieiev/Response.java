package ua.yelisieiev;

import java.io.*;

public class Response {
    private final HttpStatus status;
    private final BufferedInputStream inputStream;
    private final HttpMethod method;

    public HttpStatus getStatus() {
        return status;
    }

    public BufferedInputStream getInputStream() {
        return inputStream;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public Response(HttpMethod method, HttpStatus status, BufferedInputStream inputStream) {
        this.method = method;
        this.status = status;
        this.inputStream = inputStream;
    }
}
