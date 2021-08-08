package ua.yelisieiev.exceptions;

public class SQLExecuteException extends Exception {
    public SQLExecuteException(Exception e) {
        super(e);
    }
}
