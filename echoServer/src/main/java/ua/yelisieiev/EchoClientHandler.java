package ua.yelisieiev;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;

public interface EchoClientHandler {
    void handleClient(BufferedInputStream inputStream, BufferedOutputStream outputStream) throws IOException;
}
