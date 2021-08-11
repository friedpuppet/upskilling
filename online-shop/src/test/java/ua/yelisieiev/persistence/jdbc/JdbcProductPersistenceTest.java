package ua.yelisieiev.persistence.jdbc;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.junit.jupiter.api.BeforeEach;
import ua.yelisieiev.persistence.PersistenceException;
import ua.yelisieiev.persistence.ProductPersistenceTest;

import java.sql.SQLException;

class JdbcProductPersistenceTest extends ProductPersistenceTest {
    @BeforeEach
    void createPersistence() throws PersistenceException, SQLException {
        org.h2.jdbcx.JdbcDataSource h2DataSource = new org.h2.jdbcx.JdbcDataSource();
        h2DataSource.setURL("jdbc:h2:mem:testdb;DB_CLOSE_ON_EXIT=FALSE;DB_CLOSE_DELAY=-1");
        h2DataSource.setUser("root");
        h2DataSource.setPassword("GOD");
        // todo a funny oneliner but will the resources close?
        h2DataSource.getConnection().createStatement().execute("DROP ALL OBJECTS");

        FluentConfiguration configure = Flyway.configure();
        configure.dataSource("jdbc:h2:mem:testdb;DB_CLOSE_ON_EXIT=FALSE;DB_CLOSE_DELAY=-1", "root", "GOD");
        Flyway flyway = configure.load();
        flyway.migrate();


        setPersistence(new JdbcProductPersistence(h2DataSource));
    }
}