package ua.yelisieiev;

public class StreamsServerTest extends AbstractEchoServerTest{
    @Override
    EchoServer getServer() {
        return new StreamsEchoServer();
    }
}
