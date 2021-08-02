package ua.yelisieiev;

import java.io.*;

public class ResponseWriter {
    public static void writeResponse(OutputStream outputStream, Response response) throws IOException {
        BufferedInputStream input = response.getInputStream();
        outputStream.write(("HTTP/1.1 " + response.getStatus().toString() + "\r\n").getBytes());
        outputStream.write("Content-type: application/octet-stream\r\n".getBytes());
        outputStream.write(("Content-length: " + input.available() + "\r\n").getBytes());
        outputStream.write("\r\n".getBytes());
        input.transferTo(outputStream);
        outputStream.flush();
    }
    public static void writeErrorResponse(OutputStream outputStream, HttpStatus errorStatus) throws IOException {
        outputStream.write(("HTTP/1.1 " + errorStatus.toString() + "\r\n\r\n").getBytes());
        outputStream.flush();
    }

}
