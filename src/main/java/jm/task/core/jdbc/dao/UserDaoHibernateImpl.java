package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;

import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    private static final SessionFactory sessionFactory = Util.getSessionFactory();

    public UserDaoHibernateImpl() {

    }


    @Override
    public void createUsersTable() {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            NativeQuery query = session.createSQLQuery("CREATE TABLE Users (id BIGINT NOT NULL AUTO_INCREMENT, Name varchar(255), LastName varchar(255), Age TINYINT, PRIMARY KEY (id))");
            query.executeUpdate();
            transaction.commit();
        } catch (Exception sqlException) {
            Util.rollbackQuietly(transaction);
        }
    }

    @Override
    public void dropUsersTable() {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            NativeQuery query = session.createSQLQuery("DROP TABLE Users");
            query.executeUpdate();
            transaction.commit();
        } catch (Exception sqlException) {
            Util.rollbackQuietly(transaction);
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.save(new User(name, lastName, age));
            transaction.commit();
        } catch (Exception sqlException) {
            Util.rollbackQuietly(transaction);
        }
    }

    @Override
    public void removeUserById(long id) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.delete(getUserById(id));
            transaction.commit();
            System.out.println("\n>>> Student with id = " + id + " is successfully deleted!\n");
        } catch (Exception sqlException) {
            Util.rollbackQuietly(transaction);
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> result = null;
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            result = session.createQuery("SELECT a FROM User a", User.class).list();
            transaction.commit();
        } catch (Exception sqlException) {
            Util.rollbackQuietly(transaction);
        }
        return result;
    }

    @Override
    public void cleanUsersTable() {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.createQuery("DELETE FROM User").executeUpdate();
            transaction.commit();
        } catch (Exception sqlException) {
            Util.rollbackQuietly(transaction);
        }
    }

    public User getUserById(long id) {
        User user = null;
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            user = session.get(User.class, id);
            transaction.commit();
        } catch (Exception sqlException) {
            Util.rollbackQuietly(transaction);
        }
        return user;
    }
}
