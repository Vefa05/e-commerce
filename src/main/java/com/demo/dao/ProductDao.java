package com.demo.dao;

import com.demo.model.Product;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class ProductDao {
    private final Session session;

    public ProductDao(Session session) {
        this.session = session;
    }

    public List<Product> getAllProducts() {
        Transaction transaction = session.beginTransaction();
        List<Product> productList = null;
        try {
            productList = session.createQuery("FROM Product", Product.class).getResultList(); //from products table getting the entities based on module of the Product
            transaction.commit();
            System.out.println("Product List Size: " + productList.size()); // Log the size
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace(); // Print stack trace for debugging
        }
        return productList;
    }

    public Product getProductById(Long productId) {
        Transaction transaction = session.beginTransaction();
        Product product = null;
        try {
            product = session.get(Product.class, productId); // Fetch the product by ID
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace(); // Print stack trace for debugging
        }
        return product; // Return the found product or null if not found
    }
}
