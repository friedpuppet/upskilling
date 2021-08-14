package ua.yelisieiev;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

public abstract class AbstractEchoServer implements EchoServer {
    private int port = 8080;
    private boolean started = false;
    boolean mustStop = false;

    @Override
    public boolean isStarted() {
        return started;
    }

    @Override
    public void setPort(int port) {
        checkNotStarted();
        this.port = port;
    }

    @Override
    public void start() {
        checkNotStarted();
        Runnable mainThread = new Runnable() {
            @Override
            public void run() {
                handleRequests();
            }
        };
        new Thread(mainThread).start();
    }

    private void handleRequests() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            serverSocket.setSoTimeout(5000);
            started = true;
            serverLog("started");

            while (!mustStop) {
                try (Socket socket = serverSocket.accept();
                     BufferedInputStream inputStream = new BufferedInputStream(socket.getInputStream());
                     BufferedOutputStream outputStream = new BufferedOutputStream(socket.getOutputStream())) {
                    handleClient(inputStream, outputStream);
                } catch (SocketException e) {
                    serverLog("client closed connection");
                } catch (SocketTimeoutException e) {
                    serverLog("No client connected. Checking if must stop and starting new wait.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        started = false;
        mustStop = false;
        serverLog("stopped");
    }

    abstract void handleClient(BufferedInputStream inputStream, BufferedOutputStream outputStream) throws IOException;

    String getLine(InputStream inputStream) throws IOException {
        int byteRead = 0;
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


    void serverLog(String message) {
        System.out.println("Server: " + message);
    }

    @Override
    public void stop() {
        serverLog("stopping...");
        mustStop = true;
    }

    private void checkNotStarted() {
        if (started) {
            throw new RuntimeException("Server is already started.");
        }
    }

    @Override
    public void close() throws IOException {
        stop();
    }
}
