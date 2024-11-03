package com.demo.dao;

import com.demo.model.Order;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class OrderDao {
    private final Session session;

    public OrderDao(Session session) {
        this.session = session;
    }

    public List<Order> getOrdersByUserId(Long userId) {
        Query<Order> query = session.createQuery("FROM Order WHERE user_id = :userId", Order.class);
        query.setParameter("userId", userId);
        return query.getResultList();
    }

    public void saveOrder(Order order) {
        Transaction transaction = session.beginTransaction();
        try {
            session.save(order);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e; // Handle the exception as needed
        }
    }
}
