package ua.yelisieiev;

import ua.yelisieiev.exceptions.UnsupportedCommandException;

public class Command {
    private final CommandType type;
    private final String text;

    public Command(String text) throws UnsupportedCommandException {
        this.text = text;
        type = CommandType.getCommandTypeByName(getFirstWord(text));
    }

    public CommandType getType() {
        return type;
    }

    public String getText() {
        return text;
    }

    private String getFirstWord(String commandText) {
        int indexOfFirstSpace = commandText.indexOf(' ');
        if (indexOfFirstSpace < 0) {
            indexOfFirstSpace = commandText.length();
        }
        if (indexOfFirstSpace <= 0) {
            return "";
        }
        return commandText.substring(0, indexOfFirstSpace);
    }

}
