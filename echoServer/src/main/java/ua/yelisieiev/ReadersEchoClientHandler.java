package ua.yelisieiev;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class ReadersEchoClientHandler implements EchoClientHandler {
    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public void handleClient(BufferedInputStream inputStream, BufferedOutputStream outputStream) throws IOException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
        while (!Thread.interrupted()) {
            String line = reader.readLine();
            if (line == null) {
                log.info("client closed connection");
                break;
            }
            if (line.length() > 0) {
                log.info("received from client: " + line);
                log.info("echo: " + line);
                writer.write("echo: " + line);
                writer.write('\n');
                writer.flush();
            }
        }
    }
}
