package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    public Connection connection = Util.getConnection();

    public UserDaoJDBCImpl() {

    }

    public void createUsersTable() {
        String sql = "CREATE TABLE Users (id BIGINT NOT NULL AUTO_INCREMENT, Name varchar(255), LastName varchar(255), Age TINYINT, PRIMARY KEY (id))";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.execute();
            connection.commit();
            System.out.println("Table created");
        } catch (SQLException e) {
            Util.rollbackQuietly(connection);
            System.out.println("Table didn't create");
        }
    }

    public void dropUsersTable() {
        String sql = "DROP TABLE Users";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.execute();
            connection.commit();
            System.out.println("Table dropped");
        } catch (SQLException e) {
            Util.rollbackQuietly(connection);
            System.out.println("Table didn't drop");
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        String sql = "INSERT INTO Users (Name, Lastname, Age) VALUES (?,?,?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setInt(3, age);

            preparedStatement.execute();
            connection.commit();
            System.out.println("User с именем – " + name + " добавлен в базу данных");
        } catch (SQLException e) {
            Util.rollbackQuietly(connection);
            System.out.println("User didn't add");
        }
    }

    public void removeUserById(long id) {
        String sql = "DELETE FROM Users WHERE id=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);

            preparedStatement.execute();
            connection.commit();
            System.out.println("User deleted");
        } catch (SQLException e) {
            Util.rollbackQuietly(connection);
            System.out.println("User didn't delete");
        }
    }

    public List<User> getAllUsers() {
        List<User> result = new LinkedList<>();
        String sql = "SELECT * FROM Users";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                User usr = new User();
                usr.setId(rs.getLong("id"));
                usr.setName(rs.getString("Name"));
                usr.setLastName(rs.getString("Lastname"));
                usr.setAge(rs.getByte("Age"));
                result.add(usr);
            }

            connection.commit();
            System.out.println("List Users");
        } catch (SQLException e) {
            Util.rollbackQuietly(connection);
            System.out.println("List Users error");
        }

        return result;
    }

    public void cleanUsersTable() {
        String sql = "TRUNCATE TABLE Users";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.execute();
            connection.commit();
            System.out.println("Users cleared");
        } catch (SQLException e) {
            Util.rollbackQuietly(connection);
            System.out.println("Users didn't clear");
        }
    }
}
