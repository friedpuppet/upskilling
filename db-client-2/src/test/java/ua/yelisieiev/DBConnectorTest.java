package ua.yelisieiev;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class DBConnectorTest {

    @BeforeEach
    void setUp() {

    }

    @DisplayName("For H2 database - establish a connection to it")
    @Test
    void test_getConnectionForH2() throws ClassNotFoundException, SQLException {
        DBConnector dbConnector = new DBConnector("org.h2.Driver",
                "jdbc:h2:mem:testdb",
                "root",
                "GOD");
        try (Connection connection = dbConnector.getConnection()) {
            assertNotNull(connection);
        }
    }

    @DisplayName("For H2 file-based database - establish a connection to it")
    @Test
    void test_withRunningFileDatabase_getConnection() throws ClassNotFoundException, SQLException {
        DBConnector dbConnector = new DBConnector("org.h2.Driver",
                "jdbc:h2:./testdb",
                "root",
                "GOD");
        try (Connection connection = dbConnector.getConnection()) {
            assertNotNull(connection);
        }
    }
}