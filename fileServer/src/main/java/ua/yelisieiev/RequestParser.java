package ua.yelisieiev;

import java.io.BufferedReader;
import java.io.IOException;

public class RequestParser {
    public static Request parseRequest(BufferedReader reader) throws IOException {
        Request request = new Request();
        String[] methodParams = reader.readLine().split(" ");
        request.setUri(methodParams[1]);
        if ("GET".equals(methodParams[0])) {
            request.setMethod(HttpMethod.GET);
            String line = reader.readLine();
            while (line != null && !line.isEmpty()) {
                String[] headerString = line.split(" ");
                request.setHeader(headerString[0].substring(0, headerString[0].length() - 1),
                        headerString[1]);
                line = reader.readLine();
            }
        } else if ("POST".equals(methodParams[0])) {
            request.setMethod(HttpMethod.POST);
            // no impl here
        }
        return request;
    }
}
