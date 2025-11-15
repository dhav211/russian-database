package com.havlin.daniel.russian.entities.user_uploaded_content;

import com.havlin.daniel.russian.entities.dictionary.Word;
import jakarta.persistence.*;

@Entity
public class Chapter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int number;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    private String name;

    public static Chapter Empty(Book book) {
        Chapter emptyChapter = new Chapter();
        emptyChapter.id = 0L;
        emptyChapter.number = 0;
        emptyChapter.book = book;
        emptyChapter.name = "";

        return emptyChapter;
    }

    public Long getId() {
        return id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
