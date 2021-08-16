package ua.yelisieiev;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class UniversalEchoServer implements EchoServer {
    protected final Logger log = LoggerFactory.getLogger(this.getClass());
    private int port = 8080;
    private boolean started = false;
    private ControlThread controlThread;
    private final EchoClientHandler clientHandler;

    public UniversalEchoServer(EchoClientHandler clientHandler) {
        this.clientHandler = clientHandler;
    }

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
        controlThread = new ControlThread();
        controlThread.start();
    }

    @Override
    public void stop() {
        log.info("Stopping server...");
        controlThread.interrupt();
        try {
            controlThread.join();
        } catch (InterruptedException e) {
            log.error("Error stopping server", e);
        } finally {
            // todo join could be actually interrupted
            log.info("Server stopped");
        }
    }

    class ControlThread extends Thread {
        public ControlThread() {
            this.setName("Control thread");
        }

        @Override
        public void run() {
            log.info("Control thread started");
            started = true;

            ListenerThread listenerThread;
            try {
                listenerThread = new ListenerThread();
                // todo making the thread daemon could be useless, we have to stop it manually anyway
                listenerThread.setDaemon(true);
                listenerThread.start();
            } catch (IOException e) {
                log.error("Unable to start server", e);
                return;
            }
            while (!interrupted()) Thread.yield();
            listenerThread.interrupt();
            try {
                listenerThread.join();
            } catch (InterruptedException e) {
                log.error("Error stopping listener", e);
            }
            log.info("Control thread stopped");
            started = false;
        }
    }

    class ListenerThread extends Thread {
        private final ServerSocket serverSocket = new ServerSocket(port);

        public ListenerThread() throws IOException {
            this.setName("Listener thread");
        }

        @Override
        public void run() {
            try {
                log.info("Listener thread started");
                handleRequests(serverSocket);
            } finally {
                closeServerSocket();
                log.info("Listener thread stopped");
            }
        }

        @Override
        public void interrupt() {
            log.info("Stopping listener...");
            super.interrupt();
            closeServerSocket();
        }

        private void handleRequests(ServerSocket serverSocket) {
            while (!interrupted()) {
                try {
                    Socket socket = serverSocket.accept();
                    ClientThread clientThread = new ClientThread(socket);
                    clientThread.start();
                } catch (SocketException e) {
                    log.info("Server socket closed", e);
                } catch (IOException e) {
                    log.error("Client error", e);
                }
            }
        }

        private void closeServerSocket() {
            try {
                serverSocket.close();
            } catch (IOException e) {
                log.warn("Error closing server socket", e);
            }
        }

    }

    class ClientThread extends Thread {
        private final BufferedInputStream inputStream;
        private final BufferedOutputStream outputStream;

        private final Socket socket;

        public ClientThread(Socket socket) throws IOException {
            this.setName("Client thread " + getId());

            this.socket = socket;
            inputStream = new BufferedInputStream(socket.getInputStream());
            outputStream = new BufferedOutputStream(socket.getOutputStream());
        }

        @Override
        public void run() {
            try {
                clientHandler.handleClient(inputStream, outputStream);
                inputStream.close();
                outputStream.close();
                socket.close();
            } catch (IOException e) {
                log.error("Client error", e);
            }
        }
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
