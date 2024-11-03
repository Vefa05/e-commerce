package com.demo.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

    private static SessionFactory sessionFactory;

    // Static initializer block to build the SessionFactory
    static {
        try {
            // Build SessionFactory from the Hibernate configuration file (hibernate.cfg.xml)
            sessionFactory = new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("SessionFactory creation failed: " + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    // Method to get the current SessionFactory
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    // Method to open a new Session
    public static Session openSession() {
        return sessionFactory.openSession();
    }

    // Method to close the SessionFactory (should be called on application shutdown)
    public static void shutdown() {
        getSessionFactory().close();
    }
}