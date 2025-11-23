package com.havlin.daniel.russian.entities.users;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class LearnedWord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long wordId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private Date learnedDate;
    private Date lastSeenDate;

    private int timesPracticed;
    private int score;

    public Long getId() {
        return id;
    }

    public Long getWordId() {
        return wordId;
    }

    public void setWordId(Long wordId) {
        this.wordId = wordId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getLearnedDate() {
        return learnedDate;
    }

    public void setLearnedDate() {
        this.learnedDate = new Date();
    }

    public Date getLastSeenDate() {
        return lastSeenDate;
    }

    public void setLastSeenDate() {
        this.lastSeenDate = new Date();
    }

    public int getTimesPracticed() {
        return timesPracticed;
    }

    public void setTimesPracticed() {
        this.timesPracticed++;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
