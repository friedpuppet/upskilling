package ua.yelisieiev.exceptions;

public class SQLFetchException extends Throwable {
    public SQLFetchException(String message) {
        super(message);
    }

    public SQLFetchException(Exception e) {
        super(e);
    }
}
