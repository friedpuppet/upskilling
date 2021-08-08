package ua.yelisieiev.service;

import ua.yelisieiev.entity.Product;
import ua.yelisieiev.persistence.Persistence;
import ua.yelisieiev.persistence.PersistenceException;

import java.util.List;

public class ProductsService {
    private final Persistence persistence;

    public ProductsService(Persistence persistence) {
        this.persistence = persistence;
    }

    /**
     * takes @Product instance with @Product.id == null
     * creates corresponding object in storage
     * and fills its @Product.id with a newly generated one
     */
    public List<Product> getAll() throws ProductServiceException {
        try {
            return persistence.getAll();
        } catch (PersistenceException e) {
            throw new ProductServiceException(e);
        }
    }

    public void add(Product product) throws ProductServiceException {
        try {
            persistence.add(product);
        } catch (PersistenceException e) {
            throw new ProductServiceException(e);
        }
    }

    public Product get(Product.Id productId) throws ProductServiceException {
        try {
            return persistence.get(productId);
        } catch (PersistenceException e) {
            throw new ProductServiceException(e);
        }
    }

    public void delete(Product.Id id) throws ProductServiceException {
        try {
            persistence.delete(id);
        } catch (PersistenceException e) {
            throw new ProductServiceException(e);
        }
    }

    public void update(Product updatedProduct) throws ProductServiceException {
        try {
            persistence.update(updatedProduct);
        } catch (PersistenceException e) {
            throw new ProductServiceException(e);
        }
    }
}
