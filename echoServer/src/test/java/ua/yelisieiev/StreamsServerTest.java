package ua.yelisieiev;

public class StreamsServerTest extends UniversalEchoServerTest {
    @Override
    EchoServer getServer() {
        return new UniversalEchoServer(new StreamsEchoClientHandler());
    }
}
