package com.shopping.config;

import com.shopping.entity.Category;
import com.shopping.entity.Product;
import com.shopping.entity.User;
import com.shopping.repository.CategoryRepository;
import com.shopping.repository.ProductRepository;
import com.shopping.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(
            CategoryRepository categoryRepository,
            ProductRepository productRepository,
            UserRepository userRepository) {
        
        return args -> {
            // Check if data already exists
            if (categoryRepository.count() > 0) {
                log.info("Database already contains data. Skipping initialization.");
                return;
            }

            log.info("Initializing database with sample data...");

            // Create Categories
            Category electronics = Category.builder()
                    .name("Electronics")
                    .description("Electronic devices and accessories")
                    .build();
            categoryRepository.save(electronics);

            Category clothing = Category.builder()
                    .name("Clothing")
                    .description("Fashion and apparel")
                    .build();
            categoryRepository.save(clothing);

            Category books = Category.builder()
                    .name("Books")
                    .description("Books and publications")
                    .build();
            categoryRepository.save(books);

            // Create Products
            Product laptop = Product.builder()
                    .name("Laptop Pro 15")
                    .description("High-performance laptop with 16GB RAM and 512GB SSD")
                    .price(new BigDecimal("1299.99"))
                    .stockQuantity(50)
                    .imageUrl("https://example.com/laptop.jpg")
                    .category(electronics)
                    .isActive(true)
                    .build();
            productRepository.save(laptop);

            Product smartphone = Product.builder()
                    .name("Smartphone X")
                    .description("Latest smartphone with 5G connectivity")
                    .price(new BigDecimal("899.99"))
                    .stockQuantity(100)
                    .imageUrl("https://example.com/phone.jpg")
                    .category(electronics)
                    .isActive(true)
                    .build();
            productRepository.save(smartphone);

            Product tshirt = Product.builder()
                    .name("Classic T-Shirt")
                    .description("Comfortable cotton t-shirt")
                    .price(new BigDecimal("29.99"))
                    .stockQuantity(200)
                    .imageUrl("https://example.com/tshirt.jpg")
                    .category(clothing)
                    .isActive(true)
                    .build();
            productRepository.save(tshirt);

            Product jeans = Product.builder()
                    .name("Denim Jeans")
                    .description("Classic blue denim jeans")
                    .price(new BigDecimal("59.99"))
                    .stockQuantity(150)
                    .imageUrl("https://example.com/jeans.jpg")
                    .category(clothing)
                    .isActive(true)
                    .build();
            productRepository.save(jeans);

            Product book1 = Product.builder()
                    .name("Java Programming Guide")
                    .description("Comprehensive guide to Java programming")
                    .price(new BigDecimal("49.99"))
                    .stockQuantity(75)
                    .imageUrl("https://example.com/java-book.jpg")
                    .category(books)
                    .isActive(true)
                    .build();
            productRepository.save(book1);

            // Create Sample Users
            User user1 = User.builder()
                    .email("john.doe@example.com")
                    .name("John Doe")
                    .phone("+1-555-0100")
                    .address("123 Main St, City, Country")
                    .build();
            userRepository.save(user1);

            User user2 = User.builder()
                    .email("jane.smith@example.com")
                    .name("Jane Smith")
                    .phone("+1-555-0101")
                    .address("456 Oak Ave, City, Country")
                    .build();
            userRepository.save(user2);

            log.info("Database initialized successfully!");
            log.info("Created {} categories", categoryRepository.count());
            log.info("Created {} products", productRepository.count());
            log.info("Created {} users", userRepository.count());
        };
    }
}

