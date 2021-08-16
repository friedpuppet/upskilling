package ua.yelisieiev;

public class Starter {
    public static void main(String[] args) {
        EchoServer server = new ReadersEchoServer();
        server.setPort(8080);
        server.start();
    }
}
