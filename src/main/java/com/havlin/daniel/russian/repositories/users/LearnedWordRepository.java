package com.havlin.daniel.russian.repositories.users;

import com.havlin.daniel.russian.entities.users.LearnedWord;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface LearnedWordRepository extends JpaRepository<LearnedWord, Long> {
    @Query("SELECT lw FROM LearnedWord lw WHERE lw.wordId = :wordId AND lw.user.id = :userId")
    Optional<LearnedWord> findLearnedWordByUserAndWordId(Long userId, Long wordId);
}
