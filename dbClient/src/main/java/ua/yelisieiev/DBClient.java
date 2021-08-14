package ua.yelisieiev;

import ua.yelisieiev.config.DBClientConfig;
import ua.yelisieiev.exceptions.*;
import ua.yelisieiev.reports.HtmlTableGenerator;
import ua.yelisieiev.reports.TextTableGenerator;

import java.io.*;
import java.sql.SQLException;
import java.sql.Statement;

import static ua.yelisieiev.CommandType.*;
import static ua.yelisieiev.config.ConfigParameterName.*;

public class DBClient {
    DBClientConfig config;
    private static final String PROMPT = "> ";
    private final DBConnector dbConnector;
    private BufferedReader inputReader;
    private PrintStream outputPrintStream;
    boolean echo;

    public boolean isEcho() {
        return echo;
    }

    public void setEcho(boolean echo) {
        this.echo = echo;
    }
    TextTableGenerator textTableGenerator = new TextTableGenerator();
    HtmlTableGenerator htmlTableGenerator = new HtmlTableGenerator();

    public DBClient(DBClientConfig config) {
        try {
            if (config == null) {
                config = new DBClientConfig();
                config.load();
            } else {
                config.checkParameters();
            }
            this.config = config;

            inputReader = new BufferedReader(new InputStreamReader(System.in));
            outputPrintStream = new PrintStream(System.out);

            dbConnector = new DBConnector(config.getParam(DB_DRIVER),
                    config.getParam(DB_URL),
                    config.getParam(DB_LOGIN),
                    config.getParam(DB_PASSWORD));

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Driver " + config.getParam(DB_DRIVER) + " not found");
        }
    }

    public DBClient() {
        this(null);
    }

    public void setInput(InputStream input) {
        inputReader = new BufferedReader(new InputStreamReader(input));

    }

    public void setOutput(OutputStream output) {
        outputPrintStream = new PrintStream(output);

    }

    public void runDialog() throws IOException {
        // cycle
        outputPrintStream.println("DB url: " + config.getParam(DB_URL) + ", login: " + config.getParam(DB_LOGIN));
        CommandType commandType = null;
        while (true) {
            try {
                String commandLine = readCommand();
                if (commandLine.isEmpty()) {
                    outputPrintStream.println();
                    continue;
                }
                commandType = CommandType.getCommandTypeByName(getFirstWord(commandLine));
                if (commandType == EXIT) {
                    break;
                }
                try (Statement statement = executeCommand(commandLine)) {
                    if (commandType == SELECT) {
                        printSelectResult(statement);
                        createReportFile(statement);
                    } else if (commandType == DML) {
                        printDMLResult(statement);
                    } else if (commandType == DDL) {
                        printDDLResult(statement);
                    }
                }
            } catch (WrongCommandException e) {
                outputPrintStream.println("Invalid command: " + e.getMessage());
            } catch (ReadCommandException e) {
                outputPrintStream.println("Error reading command: " + e.getMessage());
//                e.printStackTrace();
            } catch (SQLExecuteException e) {
                outputPrintStream.println("Error executing command: " + e.getMessage());
//                e.printStackTrace();
            } catch (SQLFetchException e) {
                outputPrintStream.println("Error fetching data after execution: " + e.getMessage());
//                e.printStackTrace();
            } catch (SQLException e) {
                outputPrintStream.println("Some sql error: " + e.getMessage());
//                e.printStackTrace();
            }
        }
    }

    private void printDDLResult(Statement statement) {
        outputPrintStream.println("DDL executed");
    }

    private void createReportFile(Statement statement) {

    }

    private void printSelectResult(Statement statement) throws SQLFetchException, IOException {
        try {
            Writer writer = new BufferedWriter(new OutputStreamWriter(outputPrintStream));
            textTableGenerator.generate(statement.getResultSet(), writer);
        } catch (SQLException e) {
            throw new SQLFetchException(e);
        }
    }

    private void printDMLResult(Statement statement) throws SQLFetchException {
        try {
            outputPrintStream.println("Rows affected: " + statement.getUpdateCount());
        } catch (SQLException e) {
            throw new SQLFetchException(e);
        }
    }

    private String readCommand() throws ReadCommandException {
        try {
            outputPrintStream.print(PROMPT);
            String line = inputReader.readLine();
            if (echo) {
                outputPrintStream.println(line);
            }
            return line;
        } catch (IOException e) {
            throw new ReadCommandException(e);
        }
    }

    private Statement executeCommand(String commandText) throws SQLExecuteException {
        Statement statement;
        try {
            statement = dbConnector.getConnection().createStatement();
            statement.execute(commandText);
        } catch (SQLException e) {
            throw new SQLExecuteException(e);
        }
        return statement;
    }

    private String getFirstWord(String commandLine) {
        int indexOfFirstSpace = commandLine.indexOf(' ');
        if (indexOfFirstSpace < 0) {
            indexOfFirstSpace = commandLine.length();
        }
        if (indexOfFirstSpace <= 0) {
            return null;
        }
        return commandLine.substring(0, indexOfFirstSpace);
    }

}
