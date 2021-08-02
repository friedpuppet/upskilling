package ua.yelisieiev;

import java.io.*;

public class RequestHandler {
    private final BufferedInputStream inputStream;
    private final BufferedOutputStream outputStream;
    private final ContentReader contentReader;

    public RequestHandler(BufferedInputStream inputStream, BufferedOutputStream outputStream, String contentPath) {
        this.inputStream = inputStream;
        this.outputStream = outputStream;
        contentReader = new ContentReader(contentPath);
    }

    public void handle() {
        try {
            Request request;
            try {
                request = RequestParser.parseRequest(new BufferedReader(new InputStreamReader(inputStream)));
            } catch (IOException e) {
                ResponseWriter.writeErrorResponse(outputStream, HttpStatus.BAD_REQUEST);
                return;
            }

            BufferedInputStream contentInputStream;
            try {
                contentInputStream = contentReader.getInputStream(request.getUri());
            } catch (FileNotFoundException e) {
                ResponseWriter.writeErrorResponse(outputStream, HttpStatus.NOT_FOUND);
                return;
            }

            Response response = new Response(request.getMethod(), HttpStatus.OK, contentInputStream);

            ResponseWriter.writeResponse(outputStream, response);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
