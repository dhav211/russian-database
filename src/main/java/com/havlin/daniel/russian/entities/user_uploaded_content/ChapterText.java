package com.havlin.daniel.russian.entities.user_uploaded_content;

import jakarta.persistence.*;

@Entity
public class ChapterText {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long chapterId;

    @Lob
    private String text;

    public Long getId() {
        return id;
    }

    public Long getChapterId() {
        return chapterId;
    }

    public void setChapterId(Long chapterId) {
        this.chapterId = chapterId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
