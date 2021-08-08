package ua.yelisieiev.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ua.yelisieiev.entity.Product;
import ua.yelisieiev.persistence.jdbc.JdbcConnector;
import ua.yelisieiev.persistence.jdbc.JdbcPersistence;
import ua.yelisieiev.persistence.Persistence;
import ua.yelisieiev.persistence.PersistenceException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

public class ProductsServiceTest {

    private ProductsService productsService;

    @BeforeEach
    private void createService() throws PersistenceException, SQLException {
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

        Persistence persistence = new JdbcPersistence(new JdbcConnector(properties));
        productsService = new ProductsService(persistence);
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
