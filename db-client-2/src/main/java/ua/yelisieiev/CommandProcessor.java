package ua.yelisieiev;

import ua.yelisieiev.reports.TextTableGenerator;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.sql.ResultSet;
import java.sql.Statement;

import static ua.yelisieiev.CommandType.*;

public class CommandProcessor {
    DbBackend dbBackend;
    TextTableGenerator textTableGenerator = new TextTableGenerator();

    public CommandProcessor(DbBackend dbBackend) {
        this.dbBackend = dbBackend;
    }

    public ProcessResult process(Command command) {
        SQLResult sqlResult = dbBackend.execute(command.getText());
        String textResult;
        if (command.getType() == SELECT) {
            BufferedWriter byteWriter = new BufferedWriter(new ByteArrayOutputStream());
            textTableGenerator.generate(sqlResult.getResultSet(), byteWriter);
            textResult = byteWriter.toString();
            // TODO: make file report here; have problems if backend db doesn't support moveFirst
        } else if (command.getType() == DML) {
            textResult = "Rows affected: " + sqlResult.getRowsAffected();
        } else if (command.getType() == DDL) {
            textResult = "DDL statement executed";
        } else {
            textResult = "Statement executed";
        }

        ProcessResult processResult = new ProcessResult(command, textResult);
        return processResult;
    }
}
