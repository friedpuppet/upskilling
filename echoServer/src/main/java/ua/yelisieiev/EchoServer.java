package ua.yelisieiev;

import java.io.Closeable;

public interface EchoServer extends Closeable {

    void setPort(int port);

    void start();

    void stop();

    boolean isStarted();

}
