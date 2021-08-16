package ua.yelisieiev;

import java.io.*;

public class ReadersEchoServer extends AbstractEchoServer {
    @Override
    void handleClient(BufferedInputStream inputStream, BufferedOutputStream outputStream) throws IOException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
        while (!mustStop) {
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
