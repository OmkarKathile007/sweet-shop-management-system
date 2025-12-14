package com.sweetshop.backend.config;

import com.sweetshop.backend.model.Sweet;
import com.sweetshop.backend.repository.SweetRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(SweetRepository sweetRepository) {
        return args -> {
            // Only add data if the table is empty
            if (sweetRepository.count() == 0) {
                List<Sweet> sweets = List.of(
                        new Sweet("Kaju Katli", "Cashew", 12.50, 50, "Rich cashew fudge topped with silver leaf."),
                        new Sweet("Gulab Jamun", "Syrup", 8.00, 100, "Soft berry-sized balls dunked in rose flavored sugar syrup."),
                        new Sweet("Rasgulla", "Syrup", 7.50, 80, "Spongy cottage cheese balls soaked in light syrup."),
                        new Sweet("Mysore Pak", "Ghee", 10.00, 40, "Traditional roasted gram flour fudge made with pure ghee."),
                        new Sweet("Jalebi", "Fried", 6.00, 60, "Crispy deep-fried spirals soaked in saffron sugar syrup.")
                );

                sweetRepository.saveAll(sweets);
                System.out.println("✅ Database initialized with 5 sample sweets!");
            }
        };
    }
}