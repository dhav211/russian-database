package com.havlin.daniel.russian.entities.user_uploaded_content;

import com.havlin.daniel.russian.entities.dictionary.Translation;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String author;
    @Lob
    private String description;

    // Many to one relationship for the book

    @OneToMany(mappedBy = "book", fetch = FetchType.EAGER)
    private final Set<Chapter> chapters = new HashSet<>();

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Chapter> getChapters() {
        return chapters;
    }
}
