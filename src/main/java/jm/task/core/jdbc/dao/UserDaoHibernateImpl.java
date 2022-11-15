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
        Transaction tx1 = null;
        try (Session session = sessionFactory.openSession()) {
            tx1 = session.beginTransaction();
            NativeQuery query = session.createSQLQuery("CREATE TABLE Users (id BIGINT NOT NULL AUTO_INCREMENT, Name varchar(255), LastName varchar(255), Age TINYINT, PRIMARY KEY (id))");
            query.executeUpdate();
            tx1.commit();
        } catch (Exception sqlException) {
            Util.rollbackQuietly(tx1);
        }
    }

    @Override
    public void dropUsersTable() {
        Transaction tx1 = null;
        try (Session session = sessionFactory.openSession()) {
            tx1 = session.beginTransaction();
            NativeQuery query = session.createSQLQuery("DROP TABLE Users");
            query.executeUpdate();
            tx1.commit();
        } catch (Exception sqlException) {
            Util.rollbackQuietly(tx1);
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        Transaction tx1 = null;
        try (Session session = sessionFactory.openSession()) {
            tx1 = session.beginTransaction();
            session.save(new User(name, lastName, age));
            tx1.commit();
        } catch (Exception sqlException) {
            Util.rollbackQuietly(tx1);
        }
    }

    @Override
    public void removeUserById(long id) {
        Transaction tx1 = null;
        try (Session session = sessionFactory.openSession()) {
            tx1 = session.beginTransaction();
            session.delete(getUserById(id));
            tx1.commit();
            System.out.println("\n>>> Student with id = " + id + " is successfully deleted!\n");
        } catch (Exception sqlException) {
            Util.rollbackQuietly(tx1);
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> result = null;
        Transaction tx1 = null;
        try (Session session = sessionFactory.openSession()) {
            tx1 = session.beginTransaction();
            result = session.createQuery("FROM User", User.class).list();
            tx1.commit();
        } catch (Exception sqlException) {
            Util.rollbackQuietly(tx1);
        }
        return result;
    }

    @Override
    public void cleanUsersTable() {
        Transaction tx1 = null;
        try (Session session = sessionFactory.openSession()) {
            tx1 = session.beginTransaction();
            NativeQuery query = session.createSQLQuery("TRUNCATE TABLE Users");
            query.executeUpdate();
            tx1.commit();
        } catch (Exception sqlException) {
            Util.rollbackQuietly(tx1);
        }
    }

    public User getUserById(long id) {
        User user = null;
        Transaction tx1 = null;
        try (Session session = sessionFactory.openSession()) {
            tx1 = session.beginTransaction();
            user = session.get(User.class, id);
            tx1.commit();
        } catch (Exception sqlException) {
            Util.rollbackQuietly(tx1);
        }
        return user;
    }
}
