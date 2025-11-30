package com.havlin.daniel.russian.repositories.dictionary;

import com.havlin.daniel.russian.entities.dictionary.Word;
import com.havlin.daniel.russian.entities.dictionary.WordType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface WordRepository extends JpaRepository<Word, Long> {
    List<Word> findAllByAccented(String accented);
    List<Word> findAllByBare(String bare);

    @Query("SELECT w.type From Word w WHERE w.id = :id")
    WordType findWordTypeById(@Param("id") Long id);

    @Query("SELECT w FROM Word w " +
            "LEFT JOIN FETCH w.containingSentences " +
            "LEFT JOIN FETCH w.wordForms " +
            "LEFT JOIN FETCH w.definitions " +
            "WHERE w.accented = :accented")
    List<Word> findWordsByAccentedForContentCreation(@Param("accented") String accented);

    @Query("SELECT w FROM Word w " +
            "LEFT JOIN FETCH w.containingSentences " +
            "LEFT JOIN FETCH w.wordForms " +
            "LEFT JOIN FETCH w.definitions " +
            "WHERE w.id = :id")
    Optional<Word> findWordByIdForContentCreation(@Param("id") Long id);

    boolean existsByAccented(String accented);
}
