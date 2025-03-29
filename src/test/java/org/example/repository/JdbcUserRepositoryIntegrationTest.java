package org.example.repository;

import org.example.config.DatabaseConfig;
import org.example.model.Model;
import org.example.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class JdbcUserRepositoryIntegrationTest {

    private final DatabaseConfig databaseConfig = new DatabaseConfig();
    private JdbcUserRepository userRepository;

    @BeforeEach
    public void setUp() throws SQLException {
        userRepository = new JdbcUserRepository(databaseConfig);
    }

    @AfterEach
    public void tearDown() throws SQLException {
        String sql = "DROP ALL OBJECTS DELETE FILES";

        try (Connection connection = databaseConfig.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Couldn't tear down after test! " + e);
        }
    }

    @Test
    public void saveAndFindByIdIntegrationTest() {
        Model user = new User("test", "test@example.com");
        Model savedUser = userRepository.save(user);

        Optional<Model> foundUser = userRepository.findById(savedUser.getId());

        assertAll(
                () -> assertTrue(foundUser.isPresent()),
                () -> assertEquals(savedUser.getId(), foundUser.get().getId()),
                () -> assertEquals(savedUser.getEmail(), foundUser.get().getEmail()),
                () -> assertEquals(savedUser.getName(), foundUser.get().getName())
        );
    }

    @Test
    public void findAllIntegrationTest() {
        userRepository.save(new User("user1", "email1"));
        userRepository.save(new User("user2", "email2"));
        userRepository.save(new User("user3", "email3"));
        userRepository.save(new User("user4", "email4"));

        List<Model> users = userRepository.findAll();

        assertEquals(4, users.size());
    }

    @Test
    public void updateUserIntegrationTest() {
        Model user = userRepository.save(new User("oldName", "oldEmail"));
        user.setName("newName");
        user.setEmail("newEmail");

        Model updatedUser = userRepository.update(user);

        assertAll(
                () -> assertEquals("newName", updatedUser.getName()),
                () -> assertEquals("newEmail", updatedUser.getEmail())
        );

        Optional<Model> foundUser = userRepository.findById(user.getId());

        assertAll(
                () -> assertTrue(foundUser.isPresent()),
                () -> assertEquals("newName", foundUser.get().getName())
        );
    }

    @Test
    public void deleteUserIntegrationTest() {
        Model user = userRepository.save(new User("toDelete", "toDeleteEmail"));

        boolean isDeleted = userRepository.delete(user.getId());

        assertAll(
                () -> assertTrue(isDeleted),
                () -> assertFalse(userRepository.findById(user.getId()).isPresent())
        );
    }
}
