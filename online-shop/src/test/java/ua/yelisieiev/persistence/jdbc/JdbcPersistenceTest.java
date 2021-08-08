package ua.yelisieiev.persistence.jdbc;

import org.junit.jupiter.api.BeforeEach;
import ua.yelisieiev.persistence.PersistenceException;
import ua.yelisieiev.persistence.PersistenceTest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

class JdbcPersistenceTest extends PersistenceTest {
    @BeforeEach
    void createPersistence() throws PersistenceException, SQLException {
        Properties properties = new Properties();
        properties.setProperty("DB_URL", "jdbc:h2:./testdb");
        properties.setProperty("DB_LOGIN", "root");
        properties.setProperty("DB_PASSWORD", "GOD");
        Connection connection = DriverManager.getConnection(properties.getProperty("DB_URL"),
                properties.getProperty("DB_LOGIN"),
                properties.getProperty("DB_PASSWORD"));
        Statement statement = connection.createStatement();
        statement.execute("drop all objects");
        statement.execute("create table products (id int, name varchar(200), price number)");
        statement.close();
        connection.close();

        setPersistence(new JdbcPersistence(new JdbcConnector(properties)));
    }
}