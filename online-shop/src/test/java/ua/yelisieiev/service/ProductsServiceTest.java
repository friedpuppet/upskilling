package ua.yelisieiev.service;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ua.yelisieiev.entity.Product;
import ua.yelisieiev.persistence.jdbc.JdbcProductPersistence;
import ua.yelisieiev.persistence.ProductPersistence;
import ua.yelisieiev.persistence.PersistenceException;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class ProductsServiceTest {

    private ProductsService productsService;

    @BeforeEach
    private void createService() throws PersistenceException, SQLException {
        JdbcDataSource h2DataSource = new JdbcDataSource();
        h2DataSource.setURL("jdbc:h2:mem:testdb;DB_CLOSE_ON_EXIT=FALSE");
        h2DataSource.setUser("root");
        h2DataSource.setPassword("GOD");
        h2DataSource.getConnection().createStatement().execute("DROP ALL OBJECTS");

        FluentConfiguration configure = Flyway.configure();
        configure.dataSource("jdbc:h2:mem:testdb;DB_CLOSE_ON_EXIT=FALSE;DB_CLOSE_DELAY=-1", "root", "GOD");
        Flyway flyway = configure.load();
        flyway.migrate();

        ProductPersistence productPersistence = new JdbcProductPersistence(h2DataSource);
        productsService = new ProductsService(productPersistence);
    }

    @DisplayName("With empty persistence - add a product - and check if the service returns it")
    @Test
    void test_addProduct_checkAdded() throws ProductServiceException {
        Product milk = new Product("Milk", 35);
        productsService.add(milk);
        assertEquals(milk, productsService.get(milk.getId()));
    }

    @DisplayName("With populated persistence - delete a product - and check if it was deleted")
    @Test
    void test_deleteProduct_check() throws ProductServiceException {
        Product milk = new Product("Milk", 35);
        productsService.add(milk);
        productsService.delete(milk.getId());
        assertNull(productsService.get(milk.getId()));
    }

    @DisplayName("With populated persistence - update a product - and check if it was updated")
    @Test
    void test_updateProduct_check() throws ProductServiceException {
        Product milk = new Product("Milk", 35);
        productsService.add(milk);
        Product newMilk = new Product(milk.getId(), "PriceyMilk", 37);
        productsService.update(newMilk);
        assertEquals(newMilk, productsService.get(milk.getId()));
    }

    @DisplayName("With a persistence - perform an update for nonexistent product - get an exception")
    @Test
    void test_addInvalidProduct_throwException() {
        Product product = new Product(new Product.Id(1), "Milk", 35);

        assertThrows(ProductServiceException.class, () -> productsService.update(product));
    }

}
