package ua.yelisieiev;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class StreamsEchoClientHandler implements EchoClientHandler {
    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public void handleClient(BufferedInputStream inputStream, BufferedOutputStream outputStream) throws IOException {
        while (!Thread.interrupted()) {
            String line = getLine(inputStream);
            if (line == null) {
                log.info("client closed connection");
                break;
            }
//                        System.out.println("["  + line + "]");
            if (line.length() > 0) {
                log.info("received from client: " + line);
                log.info("echo: " + line);
                outputStream.write(("echo: " + line).getBytes());
                outputStream.write('\n');
                outputStream.flush();
            }
        }
    }

    String getLine(InputStream inputStream) throws IOException {
        int byteRead;
        try (ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {
            while (true) {
                byteRead = inputStream.read();
                if (byteRead == -1 || byteRead == '\n') {
                    break;
                }
                buffer.write(byteRead);
            }
            if (byteRead == -1 && buffer.size() == 0) {
                return null;
            }
            return buffer.toString();
        }
    }

}
