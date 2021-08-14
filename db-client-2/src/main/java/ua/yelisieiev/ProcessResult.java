package ua.yelisieiev;

public class ProcessResult {
    private final Command command;
    private final String resultAsString;

    public ProcessResult(Command command, String resultAsString) {
        this.command = command;
        this.resultAsString = resultAsString;
    }

    public Command getCommand() {
        return command;
    }

    public String getResultAsString() {
        return resultAsString;
    }
}
