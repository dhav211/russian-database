package com.havlin.daniel.russian.entities.user_uploaded_content;

import com.havlin.daniel.russian.entities.dictionary.Translation;
import jakarta.persistence.*;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY)
    private final Set<Chapter> chapters = new HashSet<>();

    public List<Chapter> getChaptersInOrder() {
        return chapters.stream().sorted(Comparator.comparingInt(Chapter::getNumber)).toList();
    }

    public Chapter getChapterByNumber(int chapterNumber) {
        for (Chapter chapter : chapters) {
            if (chapter.getNumber() == chapterNumber)
                return chapter;
        }

        return Chapter.Empty(this);
    }

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

    public void setChapters(Set<Chapter> chapters) {
        this.chapters.addAll(chapters);
    }
}
