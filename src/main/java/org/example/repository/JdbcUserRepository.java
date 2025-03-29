package org.example.repository;

import org.example.config.DatabaseConfig;
import org.example.model.Model;
import org.example.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcUserRepository implements ModelRepository {
    private final DatabaseConfig databaseConfig;

    public JdbcUserRepository(DatabaseConfig databaseConfig) {
        this.databaseConfig = databaseConfig;
    }

    @Override
    public Model save(Model user) {
        String sql = "INSERT INTO users (username, email) VALUES (?, ?)";
        try (Connection connection = databaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.executeUpdate();
            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    user.setId(resultSet.getInt(1));
                }
            }
            return user;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save user", e);
        }
    }

    @Override
    public Optional<Model> findById(int id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection connection = databaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(new User(
                            resultSet.getInt("id"),
                            resultSet.getString("username"),
                            resultSet.getString("email"))
                    );
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find user", e);
        }
    }

    @Override
    public List<Model> findAll() {
        List<Model> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try (Connection connection = databaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                users.add(new User(
                        resultSet.getInt("id"),
                        resultSet.getString("username"),
                        resultSet.getString("email")));
            }
            return users;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find all users", e);
        }
    }

    @Override
    public Model update(Model user) {
        String sql = "UPDATE users SET username = ?, email = ? WHERE id = ?";
        try (Connection connection = databaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setInt(3, user.getId());
            statement.executeUpdate();
            return user;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update user", e);
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection connection = databaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete user", e);
        }
    }
}
