package org.auth1.auth1.util;

import org.auth1.auth1.model.DatabaseManager;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;

import java.util.function.Consumer;

public class DBUtils {

    private static void performEntityOperation(final DatabaseManager databaseManager, Consumer<Session> operation) throws ConstraintViolationException {
        final SessionFactory sessionFactory = databaseManager.getSessionFactory();
        try(Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            operation.accept(session);
            session.getTransaction().commit();
        }
    }

    public static void saveOrUpdateEntity(final DatabaseManager databaseManager, Object entityObj) throws ConstraintViolationException {
        performEntityOperation(databaseManager, session -> session.saveOrUpdate(entityObj));
    }

    public static void saveEntity(final DatabaseManager databaseManager, Object entityObj) throws ConstraintViolationException {
        performEntityOperation(databaseManager, session -> session.save(entityObj));
    }

    public static void deleteEntity(final DatabaseManager databaseManager, Object entityObj) throws ConstraintViolationException {
        performEntityOperation(databaseManager, session -> session.delete(entityObj));
    }
}
