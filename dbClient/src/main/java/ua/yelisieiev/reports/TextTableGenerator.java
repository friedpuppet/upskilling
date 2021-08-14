package ua.yelisieiev.reports;

import de.vandermeer.asciitable.AsciiTable;
import ua.yelisieiev.exceptions.SQLFetchException;

import java.io.IOException;
import java.io.Writer;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class TextTableGenerator implements ReportGenerator {
    @Override
    public void generate(ResultSet resultSet, Writer writer) throws IOException, SQLFetchException {
        try {
            AsciiTable at = new AsciiTable();
            int columnCount = resultSet.getMetaData().getColumnCount();
            if (columnCount == 0) {
                throw new SQLFetchException("No columns in ResultSet");
            }
            // create header
            at.addRule();
            ArrayList<String> row = new ArrayList<>(columnCount);
            for (int i = 1; i <= columnCount; i++) {
                row.add(resultSet.getMetaData().getColumnName(i));
            }
            at.addRow(row);
            at.addRule();
            while (resultSet.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    row.set(i - 1, resultSet.getString(i));
                }
                at.addRow(row);
                at.addRule();
            }

            String renderedTable = at.render();
            writer.write(renderedTable);
            writer.write("\n");
            writer.flush();
        } catch (SQLException e) {
            throw new SQLFetchException(e);
        }
    }
}
