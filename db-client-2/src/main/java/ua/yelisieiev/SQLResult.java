package ua.yelisieiev;

import java.sql.ResultSet;

public class SQLResult {
    private final ResultSet resultSet;
    private final int rowsAffected;
    private final boolean result;

    public int getRowsAffected() {
        return rowsAffected;
    }

    public  ResultSet getResultSet() {
        return resultSet;
    }
}
