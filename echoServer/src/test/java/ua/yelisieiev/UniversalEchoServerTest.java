package ua.yelisieiev;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Пишем echo server.
 * Сервер ожидает клиентов на порту, при установлении соединения вычитывает сообщения от клиента (${content}) и отвечает на них в формате
 * "echo: ${content}".
 * Реализация должна иметь два вида
 * 1. Через Input\Output streams
 * 2. Через Reader\Writer
 * <p>
 * Думаем про закрытие ресурсов.
 * Думаем про использоваие буфферизированных потоков.
 * <p>
 * Пример:
 * https://gist.github.com/Rooman/81628d40614566bab155eb2b6c21f055
 * <p>
 * Tests:
 * server start
 * client connect
 * client sends message
 * server responds with echo
 **/

public abstract class UniversalEchoServerTest {
    private final EchoServer server = getServer();
    private static final int SERVER_PORT = 3000;

    abstract EchoServer getServer();

    private void startServer() {
        server.setPort(SERVER_PORT);
        server.start();
        while (!server.isStarted()) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    private void stopServer() {
        server.stop();
        while (server.isStarted()) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    @Test
    @DisplayName("Server successfully starts and stops")
    public void test_serverStart() {
        startServer();
        stopServer();
    }

    @Test
    @DisplayName("Start server and wait for 5 seconds")
    public void test_serverStartAndWait() {
        startServer();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopServer();
    }

    @Test
    @DisplayName("Client successfully connects and exchanges messages")
    public void test_connectAndDisconnect() throws IOException {
        startServer();
        try (Socket server = new Socket("localhost", SERVER_PORT);
             OutputStream outputStream = new BufferedOutputStream(server.getOutputStream());
             InputStream inputStream = new BufferedInputStream(server.getInputStream());) {
            clientLog("Connected");

            outputStream.write("Hello\n".getBytes());
            outputStream.flush();
            clientLog("Hello");
            String response = getLine(inputStream);
            clientLog("Server response:" + response);
            assertEquals("echo: Hello", response);

            outputStream.write("Bye\n".getBytes());
            outputStream.flush();
            clientLog("Bye");
            response = getLine(inputStream);
            clientLog("Server response: " + response);
            assertEquals("echo: Bye", response);
        } finally {
            stopServer();
        }
    }

    private void clientLog(String message) {
        System.out.println("Client: " + message);
    }

    private String getLine(InputStream inputStream) throws IOException {
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

}
