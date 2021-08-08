package ua.yelisieiev;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnector {
    private String driverName;
    private String dbUrl;
    private String user;
    private String password;

    public DBConnector(String driverName, String dbUrl, String user, String password) throws ClassNotFoundException {
        this.driverName = driverName;
        this.dbUrl = dbUrl;
        this.user = user;
        this.password = password;

        Class.forName(driverName);
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbUrl, user, password);
    }
}
