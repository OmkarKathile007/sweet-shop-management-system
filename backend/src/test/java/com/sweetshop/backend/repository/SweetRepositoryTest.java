package com.sweetshop.backend.repository;

import com.sweetshop.backend.model.Sweet;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class SweetRepositoryTest {

    @Autowired
    private SweetRepository sweetRepository;

    @Test
    void save_shouldFail_whenPriceIsNegative() {
        // Arrange
        Sweet sweet = new Sweet("Negative Candy", "Hard Candy", -10.0, 100,"");

        // Act & Assert
        // We expect this to fail, but since we haven't added @Min(0) yet,
        // it will succeed, causing the test to FAIL.
        assertThrows(ConstraintViolationException.class, () -> {
            sweetRepository.saveAndFlush(sweet);
        });
    }
}