package ua.yelisieiev.persistence;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.yelisieiev.entity.Product;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
abstract public class PersistenceTest {

    Persistence persistence;

    public void setPersistence(Persistence persistence) {
        this.persistence = persistence;
    }

    @DisplayName("On a live persistence - add a product - see that it is returned")
    @Test
    void test_addProduct_seeThatItIsReturned() throws PersistenceException {
        Product eggs = new Product("Eggs", 28);
        persistence.add(eggs);
        assertEquals(eggs, persistence.get(eggs.getId()));
    }

    @DisplayName("On a live persistence - delete an existing product  - see that it isn't returned")
    @Test
    void test_deleteProduct_seeThatItsNoMoreThere() throws PersistenceException {
        Product eggs = new Product("Eggs", 28);
        persistence.add(eggs);
        persistence.delete(eggs.getId());
        assertNull(persistence.get(eggs.getId()));
    }

    @DisplayName("On a live persistence - delete an nonexistent product  - see that no exception is thrown")
    @Test
    void test_deleteNonexistentProduct_seeThatItsOk() throws PersistenceException {
        Product eggs = new Product(new Product.Id(1), "Eggs", 28);
        persistence.delete(eggs.getId());
    }

    @DisplayName("On a live persistence - update an existing product  - see that it was changed")
    @Test
    void test_updateProduct_seeThatItsChanged() throws PersistenceException {
        Product eggs = new Product("Eggs", 28);
        persistence.add(eggs);
        Product biggerEggs = new Product(eggs.getId(), "BiggerEggs", 38);
        persistence.update(biggerEggs);
        assertEquals(biggerEggs, persistence.get(eggs.getId()));
    }

    @DisplayName("On a live persistence - update an nonexistent product  - see that it was changed")
    @Test
    void test_updateNonexistentProduct_throwException() {
        Product eggs = new Product(new Product.Id(1), "Eggs", 28);
        assertThrows(PersistenceException.class, () -> persistence.update(eggs));
    }

    @Mock
    private Persistence brokenPersistence;

    @DisplayName("On a broken persistence - try to get a product - throws exception")
    @Test
    void test_onBrokenPersistence_getProduct_throwsException() throws PersistenceException {
        // todo: it's no test, need to research more on the topic
        when(brokenPersistence.get(any())).thenThrow(PersistenceException.class);
        Product eggs = new Product(new Product.Id(1), "Eggs", 28);
        brokenPersistence.add(eggs);
        assertThrows(PersistenceException.class, () -> brokenPersistence.get(new Product.Id(1)));
    }

    @DisplayName("On a live persistence - create two products - get them")
    @Test
    void test_getAll_getTwo() throws PersistenceException {
        persistence.add(new Product("Eggs", 34));
        persistence.add(new Product("Milk", 31));
        List<Product> products = persistence.getAll();
        assertEquals(2, products.size());
    }
}