package ua.yelisieiev.persistence.jdbc;

import ua.yelisieiev.persistence.PersistenceException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

public class JdbcConnector {
    private final String dbUrl;
    private final String dbLogin;
    private final String dbPassword;

    public JdbcConnector(Properties properties) throws PersistenceException {
        dbUrl = properties.getProperty("DB_URL");
        dbLogin = properties.getProperty("DB_LOGIN");
        dbPassword = properties.getProperty("DB_PASSWORD");

        if (dbUrl == null) {
            throw new PersistenceException("DB_URL is null");
        }
        if (dbLogin == null) {
            throw new PersistenceException("DB_LOGIN is null");
        }
        if (dbPassword == null) {
            throw new PersistenceException("DB_PASSWORD is null");
        }
    }

    public Connection getConnection() throws SQLException {
        // return new connection each time; for now
        // assuming driver is implemented with auto registration within DriverManager
        return DriverManager.getConnection(dbUrl, dbLogin, dbPassword);
    }



}
