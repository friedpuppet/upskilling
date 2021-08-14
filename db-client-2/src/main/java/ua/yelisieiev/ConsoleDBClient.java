package ua.yelisieiev;

import ua.yelisieiev.config.Config;

import java.io.*;

import static ua.yelisieiev.CommandType.EMPTY;
import static ua.yelisieiev.CommandType.EXIT;

public class ConsoleDBClient {
    private Config config;
    private CommandProcessor processor;

    private BufferedReader reader;
    private PrintStream printer;
    private boolean echoMode;


    public boolean isEchoMode() {
        return echoMode;
    }

    public void setEchoMode(boolean echoMode) {
        this.echoMode = echoMode;
    }

    public void redefineInput(InputStream input) {
        reader = new BufferedReader(new InputStreamReader(input));
    }

    public void redefineOutput(OutputStream output) {
        printer = new PrintStream(output);
    }

    public ConsoleDBClient(Config config, CommandProcessor processor) {
        this.config = config;
        this.processor = processor;
    }

    public void run() {
        while (true) {
            Command command = readCommandFromInput();
            if (command.getType() == EMPTY) {
                continue;
            }
            if (command.getType() == EXIT) {
                break;
            }
            ProcessResult result = processor.process(command);
            sendResultToOutput(result);
        }
    }

    private void sendResultToOutput(ProcessResult result) {

    }

    private Command readCommandFromInput() {
        return null;
    }
}
