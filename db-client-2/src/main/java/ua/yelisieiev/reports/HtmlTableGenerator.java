package ua.yelisieiev.reports;

import ua.yelisieiev.exceptions.SQLFetchException;

import java.io.IOException;
import java.io.Writer;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.StringEscapeUtils.escapeHtml4;


/**
 * <table>
 *   <tr>
 *     <th>id</th>
 *     <th>name</th>
 *   </tr>
 *   <tr>
 *     <td>1</td>
 *     <td>Tolik</td>
 *   </tr>
 *   <tr>
 *     <td>2</td>
 *     <td>Sasha</td>
 *   </tr>
 *   <tr>
 *     <td>3</td>
 *     <td>Andrey</td>
 *   </tr>
 * </table>
 * **/
public class HtmlTableGenerator implements ReportGenerator {
    private static final String TABLE_OPEN = "<table>";
    private static final String TABLE_CLOSE = "</table>";
    private static final String ROW_OPEN = "<tr>";
    private static final String ROW_CLOSE = "</tr>";
    private static final String HEADER_OPEN = "<th>";
    private static final String HEADER_CLOSE = "</th>";
    private static final String CELL_OPEN = "<td>";
    private static final String CELL_CLOSE = "</td>";

    private void createTable(StringBuilder sb, ResultSet rs) throws SQLException {
        // open
        sb.append(TABLE_OPEN);
        // header
        int columnCount = rs.getMetaData().getColumnCount();
        ArrayList<String> row = new ArrayList<>(columnCount);
        for (int i = 0; i < columnCount; i++) {
            row.add(rs.getMetaData().getColumnName(i));
        }
        appendHeader(sb, row);
        // rows
        while (rs.next()) {
            row.clear();
            for (int i = 0; i < columnCount; i++) {
                row.add(rs.getString(i));
            }
            appendRow(sb, row);
        }
        // close
        sb.append(TABLE_CLOSE);
    }

    private void appendRow(StringBuilder sb, List<String> row) {
        sb.append(ROW_OPEN);
        for (String s : row) {
            sb.append(CELL_OPEN);
            sb.append(escapeHtml4(s));
            sb.append(CELL_CLOSE);
        }
        sb.append(ROW_CLOSE);
    }

    private void appendHeader(StringBuilder sb, List<String> row) {
        sb.append(ROW_OPEN);
        for (String s : row) {
            sb.append(HEADER_OPEN);
            sb.append(escapeHtml4(s));
            sb.append(HEADER_CLOSE);
        }
        sb.append(ROW_CLOSE);
    }

    @Override
    public void generate(ResultSet resultSet, Writer writer) throws IOException, SQLFetchException {
        try {
            StringBuilder htmlTable = new StringBuilder();
            createTable(htmlTable, resultSet);
            writer.write(htmlTable.toString());
            writer.flush();
        } catch (SQLException e) {
            throw new SQLFetchException(e);
        }
    }
}
