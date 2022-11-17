package jm.task.core.jdbc.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;

import jm.task.core.jdbc.model.User;

import java.sql.*;
import java.util.Properties;

public class Util {
    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/kata_schema";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "root";
    private static final String DB_DIALECT = "org.hibernate.dialect.MySQL8Dialect";

    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            connection.setAutoCommit(false);
            System.out.println("Connection OK");
        } catch (SQLException e) {
            System.out.println("Connection FAIL");
        }

        return connection;
    }

    public static SessionFactory getSessionFactory() {
        SessionFactory sessionFactory;

        Configuration configuration = new Configuration();

        Properties settings = new Properties();
        settings.put(Environment.DRIVER, DB_DRIVER);
        settings.put(Environment.URL, DB_URL);
        settings.put(Environment.USER, DB_USERNAME);
        settings.put(Environment.PASS, DB_PASSWORD);
        settings.put(Environment.DIALECT, DB_DIALECT);
        settings.put(Environment.SHOW_SQL, "true");
        settings.put(Environment.FORMAT_SQL, "true");
        settings.put(Environment.HBM2DDL_AUTO, "");

        try {
            configuration.setProperties(settings);
            configuration.addAnnotatedClass(User.class);
            sessionFactory = configuration.buildSessionFactory();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return sessionFactory;
    }


    public static void rollbackQuietly(Connection conn) {
        if (conn != null) {
            try {
                conn.rollback();
                System.out.println("Transaction is rolled back");
            } catch (Exception e) {
                System.out.println("Transaction doesn't rolled back");
            }
        }
    }

    public static void rollbackQuietly(Transaction transaction) {
        if (transaction != null) {
            try {
                transaction.rollback();
                System.out.println("Transaction is rolled back");
            } catch (Exception e) {
                System.out.println("Transaction doesn't rolled back");
            }
        }
    }

    public static void closeQuietly (Session session) {
        if (session != null) {
            try {
                session.close();
            } catch (Exception e) {
                //
            }
        }
    }
}
