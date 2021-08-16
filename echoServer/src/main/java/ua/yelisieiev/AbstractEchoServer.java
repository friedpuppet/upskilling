package ua.yelisieiev;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

public abstract class AbstractEchoServer implements EchoServer {
    protected final Logger log = LoggerFactory.getLogger(this.getClass());
    private int port = 8080;
    private boolean started = false;
    boolean mustStop = false;
    private ControlThread controlThread;

    @Override
    public boolean isStarted() {
        return started;
    }

    @Override
    public void setPort(int port) {
        checkNotStarted();
        this.port = port;
    }

    class ControlThread extends Thread {
        public ControlThread() {
            this.setName("Control thread");
        }

        @Override
        public void run() {
            log.info("Control thread started");
            MainThread mainThread = new MainThread();
            mainThread.setDaemon(true);
            mainThread.start();
            while (!interrupted());
            log.info("Control thread stopped");
        }
    }

    class MainThread extends Thread {
        public MainThread() {
            this.setName("Main thread");
        }

        @Override
        public void run() {
            handleRequests();
        }
    }

    class RequestThread extends Thread {
        private final BufferedInputStream inputStream;
        private final BufferedOutputStream outputStream;
        private final Socket socket;

        public RequestThread(Socket socket) throws IOException {
            this.setName("Client thread " + getId());

            this.socket = socket;
            inputStream = new BufferedInputStream(socket.getInputStream());
            outputStream = new BufferedOutputStream(socket.getOutputStream());
        }

        @Override
        public void run() {
            try {
                handleClient(inputStream, outputStream);
                inputStream.close();
                outputStream.close();
                socket.close();
            } catch (IOException e) {
                log.error("Client error", e);
            }
        }
    }

    @Override
    public void start() {
        checkNotStarted();
        controlThread = new ControlThread();
        controlThread.start();
    }

    private void handleRequests() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            started = true;
            log.info("started");

            while (!mustStop) {
                try {
                    Socket socket = serverSocket.accept();
                    RequestThread requestThread = new RequestThread(socket);
                    requestThread.start();
                } catch (SocketException e) {
                    log.info("client closed connection");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        started = false;
        mustStop = false;
        log.info("stopped");
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

    @Override
    public void stop() {
        log.info("stopping...");
        controlThread.interrupt();
        mustStop = true;
    }

    private void checkNotStarted() {
        if (started) {
            throw new RuntimeException("Server is already started.");
        }
    }

    @Override
    public void close() {
        stop();
    }
}
