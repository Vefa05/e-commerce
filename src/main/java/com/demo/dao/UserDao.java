package com.demo.dao;

import com.demo.model.User;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class UserDao {
    private final Session session;

    public UserDao(Session session) {
        this.session = session;
    }

    public void updateUser(User user) {
        Transaction transaction = session.beginTransaction();
        try {
            session.update(user);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e; // Handle exceptions appropriately
        }
    }

    public User getUserById(Long userId) {
        return session.get(User.class, userId);
    }
}
