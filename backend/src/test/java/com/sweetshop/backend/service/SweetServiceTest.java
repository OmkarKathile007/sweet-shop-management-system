package com.sweetshop.backend.service;

import com.sweetshop.backend.model.Sweet;
import com.sweetshop.backend.repository.SweetRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SweetServiceTest {

    @Mock
    private SweetRepository sweetRepository;

    @InjectMocks
    private SweetService sweetService;

    @Test
    void addSweet_shouldSaveAndReturnSweet() {
        // Arrange
        Sweet inputSweet = new Sweet("Laddu", "Traditional", 10.0, 50);
        Sweet savedSweet = new Sweet("Laddu", "Traditional", 10.0, 50);
        savedSweet.setId(1L);

        when(sweetRepository.save(any(Sweet.class))).thenReturn(savedSweet);

        // Act
        Sweet result = sweetService.addSweet(inputSweet);

        // Assert
        assertNotNull(result, "Service returned null");
        assertEquals(1L, result.getId());
        verify(sweetRepository).save(inputSweet);
    }
}