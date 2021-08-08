package ua.yelisieiev.entity;

import java.text.MessageFormat;
import java.util.Objects;

public class Product {
    private Id id;
    private String name;
    private double price;

    public Product(String name, double price) {
        this(null, name, price);
    }

    public Product(Id id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public void setId(Id id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Id getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return MessageFormat.format("'{'id={0}, name=''{1}'', price={2}'}'", id, name, price);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Double.compare(product.price, price) == 0 && Objects.equals(id, product.id) && Objects.equals(name, product.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price);
    }

    public static class Id {
        private final int value;

        public Id(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Id id1 = (Id) o;
            return value == id1.value;
        }

        @Override
        public int hashCode() {
            return Objects.hash(value);
        }
    }
}
