package ua.yelisieiev;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

public class StreamsEchoServer extends AbstractEchoServer {
    @Override
    void handleClient(BufferedInputStream inputStream, BufferedOutputStream outputStream) throws IOException {
        while (!mustStop) {
            String line = getLine(inputStream);
            if (line == null) {
                serverLog("client closed connection");
                break;
            }
//                        System.out.println("["  + line + "]");
            if (line.length() > 0) {
                serverLog("received from client: " + line);
                serverLog("echo: " + line);
                outputStream.write(("echo: " + line).getBytes());
                outputStream.write('\n');
                outputStream.flush();
            }
        }
    }
}
