package ua.yelisieiev;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

public class FileServer {
    public static final int DEFAULT_PORT = 8080;
    public static final String DEFAULT_CONTENT_PATH = "./files";

    private int port;
    private String contentPath;
    private boolean started;
    private boolean mustStop;
    private Thread thread;

    public boolean isStarted() {
        return started;
    }

    public FileServer(int port, String contentPath) {
        this.port = port;
        this.contentPath = contentPath;
    }

    public FileServer() {
        this(DEFAULT_PORT, DEFAULT_CONTENT_PATH);
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setContentPath(String path) {
        this.contentPath = path;
    }

    public void start() {
        checkNotStarted();
//        Runnable mainThread = this::handleRequests;
        thread = new Thread(this::handleRequests);
//        thread.setDaemon(true);
        thread.start();
    }


    private void handleRequests() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            serverSocket.setSoTimeout(5000);
            started = true;
            serverLog("started on port " + port + " and directory " + contentPath);

            while (!mustStop) {
                try (Socket socket = serverSocket.accept();
                     BufferedInputStream inputStream = new BufferedInputStream(socket.getInputStream());
                     BufferedOutputStream outputStream = new BufferedOutputStream(socket.getOutputStream())) {
                    RequestHandler requestHandler = new RequestHandler(inputStream, outputStream, contentPath);
                    serverLog("Connected from: " + socket.getInetAddress());
                    requestHandler.handle();
                } catch (SocketException e) {
                    serverLog("client closed connection");
                } catch (SocketTimeoutException e) {
//                    serverLog("No client connected. Checking if must stop and starting new wait.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        started = false;
        mustStop = false;
        serverLog("stopped");
    }

    void serverLog(String message) {
        System.out.println("Server: " + message);
    }

    public void stop() {
        serverLog("stopping...");
        mustStop = true;
    }

    private void checkNotStarted() {
        if (started) {
            throw new RuntimeException("Server is already started");
        }
    }
}
