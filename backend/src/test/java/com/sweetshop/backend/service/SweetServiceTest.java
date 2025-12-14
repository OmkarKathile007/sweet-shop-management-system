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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class SweetServiceTest {

    @Mock
    private SweetRepository sweetRepository;

    @InjectMocks
    private SweetService sweetService;

    @Test
    void addSweet_shouldSaveAndReturnSweet() {
        // Arrange
        Sweet inputSweet = new Sweet("Laddu", "Traditional", 10.0, 50,"");
        Sweet savedSweet = new Sweet("Laddu", "Traditional", 10.0, 50,"");
        savedSweet.setId(1L);

        when(sweetRepository.save(any(Sweet.class))).thenReturn(savedSweet);

        // Act
        Sweet result = sweetService.addSweet(inputSweet);

        // Assert
        assertNotNull(result, "Service returned null");
        assertEquals(1L, result.getId());
        verify(sweetRepository).save(inputSweet);
    }
    @Test
    void getAllSweets_shouldReturnListOfSweets() {
        // Arrange
        Sweet s1 = new Sweet("S1", "C1", 10.0, 10,"");
        Sweet s2 = new Sweet("S2", "C2", 20.0, 20,"");
        when(sweetRepository.findAll()).thenReturn(Arrays.asList(s1, s2));

        // Act
        List<Sweet> result = sweetService.getAllSweets();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void getSweetById_shouldReturnSweet_whenFound() {
        // Arrange
        Sweet sweet = new Sweet("Laddu", "Trad", 10.0, 50,"");
        when(sweetRepository.findById(1L)).thenReturn(Optional.of(sweet));

        // Act
        Sweet result = sweetService.getSweetById(1L);

        // Assert
        assertNotNull(result);
        assertEquals("Laddu", result.getName());
    }

    @Test
    void getSweetById_shouldThrowException_whenNotFound() {
        // Arrange
        when(sweetRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            sweetService.getSweetById(99L);
        });
    }

    @Test
    void updateSweet_shouldUpdateFields_whenFound() {
        // Arrange
        Long id = 1L;
        Sweet existingSweet = new Sweet("Old Name", "Old Cat", 10.0, 10,"");
        existingSweet.setId(id);

        Sweet updateDetails = new Sweet("New Name", "New Cat", 20.0, 20,"");

        when(sweetRepository.findById(id)).thenReturn(Optional.of(existingSweet));
        when(sweetRepository.save(existingSweet)).thenReturn(existingSweet);

        // Act
        Sweet updatedSweet = sweetService.updateSweet(id, updateDetails);

        // Assert
        assertNotNull(updatedSweet);
        assertEquals("New Name", updatedSweet.getName());
        assertEquals("New Cat", updatedSweet.getCategory());
        assertEquals(20.0, updatedSweet.getPrice());
    }

    @Test
    void deleteSweet_shouldCallRepositoryDelete_whenFound() {
        // Arrange
        Long id = 1L;
        Sweet sweet = new Sweet("To Delete", "Cat", 10.0, 10,"");
        when(sweetRepository.findById(id)).thenReturn(Optional.of(sweet));

        // Act
        sweetService.deleteSweet(id);

        // Assert
        verify(sweetRepository).delete(sweet);
    }
}