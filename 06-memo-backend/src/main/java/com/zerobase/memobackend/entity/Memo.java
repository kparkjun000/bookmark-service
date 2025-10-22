package com.zerobase.memobackend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "memos", indexes = {
    @Index(name = "idx_memo_title", columnList = "title"),
    @Index(name = "idx_memo_created_at", columnList = "created_at")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Memo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(length = 100)
    private String author;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "memo_tags",
        joinColumns = @JoinColumn(name = "memo_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    @Builder.Default
    private Set<Tag> tags = new HashSet<>();

    @OneToMany(mappedBy = "memo", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<SharedMemo> sharedMemos = new HashSet<>();

    public void addTag(Tag tag) {
        this.tags.add(tag);
        tag.getMemos().add(this);
    }

    public void removeTag(Tag tag) {
        this.tags.remove(tag);
        tag.getMemos().remove(this);
    }

    public void clearTags() {
        for (Tag tag : new HashSet<>(tags)) {
            removeTag(tag);
        }
    }

}

