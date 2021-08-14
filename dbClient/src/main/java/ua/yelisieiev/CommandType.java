package ua.yelisieiev;

import ua.yelisieiev.exceptions.WrongCommandException;

import java.util.Set;

public enum CommandType {
    SELECT,
    DML,
    DDL,
    EXIT,
    SHUTDOWN;

    public static CommandType getCommandTypeByName(String name) throws WrongCommandException {
        if (name == null) {
            throw new WrongCommandException("Null command");
        }
        if ("SELECT".equalsIgnoreCase(name)) {
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
            throw new WrongCommandException("Command [" + name + "] not supported");
        }
    }
}
