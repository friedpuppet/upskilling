package ua.yelisieiev;

public enum HttpStatus {
    OK(200, "OK"),
    BAD_REQUEST(400, "Bad request"),
    NOT_FOUND(404, "Not found");

    private final int Code;
    private final String Message;

    HttpStatus(int code, String message) {
        Code = code;
        Message = message;
    }

    @Override
    public String toString() {
        return Code + " " + Message;
    }
}
