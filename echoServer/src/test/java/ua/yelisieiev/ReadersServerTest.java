package ua.yelisieiev;

public class ReadersServerTest extends AbstractEchoServerTest {
    @Override
    EchoServer getServer() {
        return new ReadersEchoServer();
    }
}
