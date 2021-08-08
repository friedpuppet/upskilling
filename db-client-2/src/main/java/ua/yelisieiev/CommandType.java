package ua.yelisieiev;

import ua.yelisieiev.exceptions.UnsupportedCommandException;

import java.util.Set;

public enum CommandType {
    SELECT,
    DML,
    DDL,
    EXIT,
    SHUTDOWN,
    EMPTY;

    public static CommandType getCommandTypeByName(String name) throws UnsupportedCommandException {
        if (name == null || name.isEmpty()) {
            return EMPTY;
        } else if ("SELECT".equalsIgnoreCase(name)) {
            return SELECT;
        } else if ("INSERT".equalsIgnoreCase(name) ||
                "UPDATE".equalsIgnoreCase(name) ||
                "DELETE".equalsIgnoreCase(name)) {
            return DML;
        } else if (Set.of("CREATE", "DROP").contains(name.toUpperCase())) {
            return DDL;
        } else if ("SHUTDOWN".equalsIgnoreCase(name)) {
            return SHUTDOWN;
        } else if ("EXIT".equalsIgnoreCase(name)) {
            return EXIT;
        } else {
            throw new UnsupportedCommandException("Command [" + name + "] not supported");
        }
    }
}
