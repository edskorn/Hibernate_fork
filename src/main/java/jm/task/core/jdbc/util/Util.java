package jm.task.core.jdbc.util;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;

import jm.task.core.jdbc.model.User;
import org.hibernate.service.ServiceRegistry;

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
            //Class.forName(DB_DRIVER);
            connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            connection.setAutoCommit(false);
            System.out.println("Connection OK");
        } catch (SQLException /*| ClassNotFoundException*/ e) {
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
        settings.put(Environment.HBM2DDL_AUTO, "");

        try {

            configuration.setProperties(settings);

            configuration.addAnnotatedClass(User.class);

            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties()).build();

            sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return sessionFactory;
    }

    public static void CloseQuietly(Connection conn) {
        try {
            conn.close();
        } catch (Exception e) {
            //
        }
    }

    public static void CloseQuietly(Statement stmt) {
        try {
            stmt.close();
        } catch (Exception e) {
            //
        }
    }

    public static void CloseQuietly(ResultSet rs) {
        try {
            rs.close();
        } catch (Exception e) {
            //
        }
    }

    public static void RollbackQuietly(Connection conn) {
        try {
            conn.rollback();
        } catch (Exception e) {
            //
        }
    }
}
