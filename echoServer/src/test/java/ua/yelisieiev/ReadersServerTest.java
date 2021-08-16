package ua.yelisieiev;

public class ReadersServerTest extends UniversalEchoServerTest {
    @Override
    EchoServer getServer() {
        return new UniversalEchoServer(new ReadersEchoClientHandler());
    }
}
