package ua.yelisieiev.exceptions;

import java.io.IOException;

public class PrintOutException extends Throwable {
    public PrintOutException(Throwable e) {
        super(e);
    }
}
