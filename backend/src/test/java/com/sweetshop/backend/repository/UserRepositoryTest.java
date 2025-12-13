package com.sweetshop.backend.repository;

import com.sweetshop.backend.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // Use real Postgres
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void testUsernameMustBeUnique() {
        // Arrange
        User user1 = new User("testuser", "password123", "USER");
        userRepository.save(user1);

        // Act & Assert
        // We try to save a second user with the EXACT SAME username.
        // We EXPECT this to fail with DataIntegrityViolationException.
        User user2 = new User("testuser", "password456", "USER");

        assertThrows(DataIntegrityViolationException.class, () -> {
            userRepository.saveAndFlush(user2);
        });
    }
}