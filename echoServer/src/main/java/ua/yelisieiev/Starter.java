package ua.yelisieiev;

public class Starter {
    public static void main(String[] args) {
        EchoServer server = new UniversalEchoServer(new ReadersEchoClientHandler());
        server.setPort(8080);
        server.start();
    }
}
