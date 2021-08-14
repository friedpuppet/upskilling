package ua.yelisieiev.exceptions;

public class ReadCommandException extends Throwable {
    public ReadCommandException(Exception e) {
        super(e);
    }
}
