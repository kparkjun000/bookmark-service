package com.zerobase.bookmarkservice.config;

import com.zerobase.bookmarkservice.entity.Bookmark;
import com.zerobase.bookmarkservice.entity.Tag;
import com.zerobase.bookmarkservice.entity.User;
import com.zerobase.bookmarkservice.repository.BookmarkRepository;
import com.zerobase.bookmarkservice.repository.TagRepository;
import com.zerobase.bookmarkservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    
    private final UserRepository userRepository;
    private final BookmarkRepository bookmarkRepository;
    private final TagRepository tagRepository;
    
    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            log.info("Initializing sample data...");
            initializeSampleData();
            log.info("Sample data initialized successfully");
        } else {
            log.info("Database already contains data, skipping initialization");
        }
    }
    
    private void initializeSampleData() {
        // Create sample user
        User user = User.builder()
                .email("demo@example.com")
                .name("데모 사용자")
                .build();
        user = userRepository.save(user);
        
        // Create sample tags
        Tag developmentTag = Tag.builder()
                .name("개발")
                .user(user)
                .build();
        developmentTag = tagRepository.save(developmentTag);
        
        Tag referenceTag = Tag.builder()
                .name("참고자료")
                .user(user)
                .build();
        referenceTag = tagRepository.save(referenceTag);
        
        Tag tutorialTag = Tag.builder()
                .name("튜토리얼")
                .user(user)
                .build();
        tutorialTag = tagRepository.save(tutorialTag);
        
        // Create sample bookmarks
        Set<Tag> tags1 = new HashSet<>();
        tags1.add(developmentTag);
        tags1.add(referenceTag);
        
        Bookmark bookmark1 = Bookmark.builder()
                .url("https://spring.io/projects/spring-boot")
                .title("Spring Boot")
                .description("Spring Boot makes it easy to create stand-alone, production-grade Spring based Applications")
                .siteName("Spring")
                .isPublic(true)
                .isFavorite(true)
                .user(user)
                .tags(tags1)
                .build();
        bookmarkRepository.save(bookmark1);
        
        Set<Tag> tags2 = new HashSet<>();
        tags2.add(developmentTag);
        tags2.add(tutorialTag);
        
        Bookmark bookmark2 = Bookmark.builder()
                .url("https://www.postgresql.org/docs/")
                .title("PostgreSQL Documentation")
                .description("The official documentation for PostgreSQL")
                .siteName("PostgreSQL")
                .isPublic(true)
                .isFavorite(false)
                .user(user)
                .tags(tags2)
                .build();
        bookmarkRepository.save(bookmark2);
        
        Set<Tag> tags3 = new HashSet<>();
        tags3.add(referenceTag);
        
        Bookmark bookmark3 = Bookmark.builder()
                .url("https://github.com/")
                .title("GitHub")
                .description("Where the world builds software")
                .siteName("GitHub")
                .isPublic(false)
                .isFavorite(true)
                .user(user)
                .tags(tags3)
                .build();
        bookmarkRepository.save(bookmark3);
        
        log.info("Created sample user: {}", user.getEmail());
        log.info("Created {} bookmarks", bookmarkRepository.count());
        log.info("Created {} tags", tagRepository.count());
    }
}

