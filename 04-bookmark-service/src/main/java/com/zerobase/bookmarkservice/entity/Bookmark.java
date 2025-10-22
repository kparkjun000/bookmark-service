package com.zerobase.bookmarkservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "bookmarks", indexes = {
    @Index(name = "idx_user_id", columnList = "user_id"),
    @Index(name = "idx_created_at", columnList = "created_at")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bookmark extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 2048)
    private String url;
    
    @Column(length = 500)
    private String title;
    
    @Column(length = 2000)
    private String description;
    
    @Column(length = 500)
    private String imageUrl;
    
    @Column(length = 100)
    private String siteName;
    
    @Builder.Default
    @Column(nullable = false)
    private Boolean isPublic = false;
    
    @Builder.Default
    @Column(nullable = false)
    private Boolean isFavorite = false;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;
    
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "bookmark_tags",
        joinColumns = @JoinColumn(name = "bookmark_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    @Builder.Default
    private Set<Tag> tags = new HashSet<>();
    
    public void addTag(Tag tag) {
        this.tags.add(tag);
        tag.getBookmarks().add(this);
    }
    
    public void removeTag(Tag tag) {
        this.tags.remove(tag);
        tag.getBookmarks().remove(this);
    }
}

