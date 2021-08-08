package ua.yelisieiev.reports;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.yelisieiev.reports.ReportGenerator;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
abstract class AbstractGeneratorTest {
    @Mock
    ResultSet goodResultSet;
    @Mock
    ResultSetMetaData goodMetaData;

    @Mock
    ResultSet badResultSet;

    @Mock
    Writer badWriter;
    private ReportGenerator generator = getGenerator();

    abstract ReportGenerator getGenerator();
    abstract String getExpectedTable();

    @DisplayName("With correct ResultSet - write correct table to provided writer")
    @Test
    void test_withGoodResultSet_writeThreeRowsTable() throws SQLException, IOException {
        prepareGoodResultSet();

        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        Writer writer = new OutputStreamWriter(byteOut);

        try {
            generator.generate(goodResultSet, writer);
        } catch (ua.yelisieiev.exceptions.SQLFetchException e) {
            e.printStackTrace();
        }
        String expectedTable = getExpectedTable();

        assertEquals(expectedTable, byteOut.toString());
    }

    @DisplayName("With bad ResultSet - throw SQLException")
    @Test
    void test_withBadResultSet_throwException() throws SQLException {
        prepareBadResultSet();

        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        Writer writer = new OutputStreamWriter(byteOut);

        assertThrows(SQLException.class, () -> generator.generate(badResultSet, writer));
    }

    @DisplayName("With bad writer - get IOException")
    @Test
    void test_badWriter_getIOException() throws SQLException, IOException {
        prepareGoodResultSet();
        prepareBadWiter();

        assertThrows(IOException.class, () -> generator.generate(goodResultSet, badWriter));
    }

    private void prepareGoodResultSet() throws SQLException {
        when(goodResultSet.getMetaData()).thenReturn(goodMetaData);
        when(goodMetaData.getColumnCount()).thenReturn(3);
        when(goodMetaData.getColumnName(0)).thenReturn("First");
        when(goodMetaData.getColumnName(1)).thenReturn("Second");
        when(goodMetaData.getColumnName(2)).thenReturn("Third");
        when(goodResultSet.next()).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(false);
        when(goodResultSet.getString(0)).thenReturn("1").thenReturn("2").thenReturn("3");
        when(goodResultSet.getString(1)).thenReturn("Bob").thenReturn("Alice").thenReturn("Jess");
        when(goodResultSet.getString(2)).thenReturn("12").thenReturn("22").thenReturn("13");
    }

    private void prepareBadResultSet() throws SQLException {
        when(badResultSet.getMetaData()).thenThrow(new SQLException("Hello from mockito"));
    }

    private void prepareBadWiter() throws IOException {
        doThrow(new IOException("Hello from mockito")).when(badWriter).write(anyString());
    }
}