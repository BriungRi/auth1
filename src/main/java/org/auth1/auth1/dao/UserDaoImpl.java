package org.auth1.auth1.dao;

import org.auth1.auth1.model.DatabaseManager;
import org.auth1.auth1.model.entities.User;
import org.auth1.auth1.util.DBUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.NotYetImplementedException;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserDaoImpl implements UserDao {

    private final DatabaseManager databaseManager;

    public UserDaoImpl(final DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    @Override
    public boolean login(String username, String password) {
        Optional<User> user = getUserByUsername(username);
        return user.map(user1 -> user1.getPassword().equals(password)).orElse(false);
    }

    public void register(final User user) {
        final SessionFactory sessionFactory = databaseManager.getSessionFactory();
        final Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.save(user);
        session.getTransaction().commit();
    }

    @Override
    public void setPasswordResetToken(String username, String passwordResetToken) {
        throw new NotYetImplementedException();
    }

    @Override
    public void lockUser(String username) {
        throw new NotYetImplementedException();
    }

    @Override
    public void unlockUser(String username) {
        throw new NotYetImplementedException();
    }

    @Override
    public void resetPassword(String username, String password) {
        throw new NotYetImplementedException();
    }

    @Override
    public Optional<User> getUserByUsername(String username) {
        return DBUtil.getFirstRow(databaseManager, User.class, "username", username);
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return DBUtil.getFirstRow(databaseManager, User.class, "email", email);
    }
}
