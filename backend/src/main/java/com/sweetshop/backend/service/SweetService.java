package com.sweetshop.backend.service;

import com.sweetshop.backend.model.Sweet;
import com.sweetshop.backend.repository.SweetRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.List;


@Service
public class SweetService {

    private final SweetRepository sweetRepository;

    public SweetService(SweetRepository sweetRepository) {
        this.sweetRepository = sweetRepository;
    }

    public Sweet addSweet(Sweet sweet) {
        return sweetRepository.save(sweet);
    }

    public List<Sweet> getAllSweets() {
        return sweetRepository.findAll();
    }
    public Sweet getSweetById(Long id) {
        return sweetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sweet not found with id: " + id));
    }
    public Sweet updateSweet(Long id, Sweet sweetDetails) {
        Sweet sweet = sweetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sweet not found with id: " + id));

        sweet.setName(sweetDetails.getName());
        sweet.setPrice(sweetDetails.getPrice());
        sweet.setDescription(sweetDetails.getDescription());
        // sweet.setImageUrl(...); // REMOVED

        return sweetRepository.save(sweet);
    }

    public Sweet restockSweet(Long id, int quantityToAdd) {
        Sweet sweet = sweetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sweet not found with id: " + id));

        if (quantityToAdd <= 0) {
            throw new IllegalArgumentException("Restock quantity must be positive");
        }

        sweet.setQuantity(sweet.getQuantity() + quantityToAdd);
        return sweetRepository.save(sweet);
    }

    public void deleteSweet(Long id) {
        Sweet sweet = getSweetById(id);
        sweetRepository.delete(sweet);
    }

    public List<Sweet> searchSweets(String name, String category, Double minPrice, Double maxPrice) {

        String namePattern = (name != null && !name.isEmpty()) ? "%" + name.toLowerCase() + "%" : null;
        String categoryPattern = (category != null && !category.isEmpty()) ? "%" + category.toLowerCase() + "%" : null;

        return sweetRepository.searchSweets(namePattern, categoryPattern, minPrice, maxPrice);

    }
}