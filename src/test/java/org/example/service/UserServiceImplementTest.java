package org.example.service;

import org.example.model.Model;
import org.example.model.User;
import org.example.repository.JdbcUserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private JdbcUserRepository userRepositoryMock;
    @InjectMocks
    private UserServiceImplement userService;

    @Test
    void saveUser() {
        User user = new User("test", "test@example.com");
        when(userRepositoryMock.save(user)).thenReturn(user);
        assertEquals(user, userService.save(user));
        verify(userRepositoryMock).save(user);
    }

    @Test
    void findById() {
        User user = new User(1, "test", "test@example.com");
        when(userRepositoryMock.findById(1)).thenReturn(Optional.of(user));
        assertEquals(user, userService.findById(1).orElse(null));
        verify(userRepositoryMock).findById(1);
    }

    @Test
    void findAll() {
        List<Model> users = List.of(new User("test", "test@example.com"));
        when(userRepositoryMock.findAll()).thenReturn(users);
        assertEquals(users, userService.findAll());
        verify(userRepositoryMock).findAll();
    }

    @Test
    void update() {
        User user = new User(1, "new", "new@example.com");
        when(userRepositoryMock.update(user)).thenReturn(user);
        assertEquals(user, userService.update(user));
        verify(userRepositoryMock).update(user);
    }

    @Test
    void delete() {
        when(userRepositoryMock.delete(1)).thenReturn(true);
        assertTrue(userService.delete(1));
        verify(userRepositoryMock).delete(1);
    }
}