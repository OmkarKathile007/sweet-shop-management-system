package com.sweetshop.backend.controller;

import com.sweetshop.backend.model.Sweet;
import com.sweetshop.backend.service.SweetService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;


@CrossOrigin
@RestController
@RequestMapping("/api/sweets")
public class SweetController {

    private final SweetService sweetService;

    public SweetController(SweetService sweetService) {
        this.sweetService = sweetService;
    }

    @PostMapping
    public ResponseEntity<Sweet> addSweet(@RequestBody Sweet sweet) {
        return ResponseEntity.ok(sweetService.addSweet(sweet));
    }

    @GetMapping
    public ResponseEntity<List<Sweet>> getAllSweets() {
        return ResponseEntity.ok(sweetService.getAllSweets());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sweet> getSweetById(@PathVariable Long id) {
        return ResponseEntity.ok(sweetService.getSweetById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Sweet> updateSweet(@PathVariable Long id, @RequestBody Sweet sweetDetails) {
        Sweet updatedSweet = sweetService.updateSweet(id, sweetDetails);
        return ResponseEntity.ok(updatedSweet);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSweet(@PathVariable Long id) {
        try {
            sweetService.deleteSweet(id); // Assuming this calls repo.deleteById(id)
            return ResponseEntity.noContent().build();
        } catch (DataIntegrityViolationException e) {
            // This catches the specific SQL error 23503
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Cannot delete this sweet because it has been ordered by customers. Please set the Quantity to 0 instead.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while deleting the sweet.");
        }
    }

    @PostMapping("/{id}/restock")
    public ResponseEntity<Sweet> restockSweet(@PathVariable Long id, @RequestParam int quantity) {
        // Usage: POST /api/sweets/5/restock?quantity=50
        Sweet restockedSweet = sweetService.restockSweet(id, quantity);
        return ResponseEntity.ok(restockedSweet);
    }

    @GetMapping("/search")
    public List<Sweet> searchSweets(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice
    ) {
        return sweetService.searchSweets(name, category, minPrice, maxPrice);
    }
}