package ua.yelisieiev.persistence;

import ua.yelisieiev.entity.Product;

import java.util.List;

public interface Persistence {
    List<Product> getAll() throws PersistenceException;

    void add(Product product) throws PersistenceException;

    Product get(Product.Id productId) throws PersistenceException;

    void delete(Product.Id productId) throws PersistenceException;

    void update(Product updatedProduct) throws PersistenceException;
}
