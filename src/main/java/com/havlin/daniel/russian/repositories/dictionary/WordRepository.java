package com.havlin.daniel.russian.repositories.dictionary;

import com.havlin.daniel.russian.entities.dictionary.Word;
import com.havlin.daniel.russian.entities.dictionary.WordType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WordRepository extends JpaRepository<Word, Long> {
    List<Word> findAllByAccented(String accented);
    List<Word> findAllByBare(String bare);

    @Query("SELECT w.type From Word w WHERE w.id = :id")
    WordType findWordTypeById(@Param("id") Long id);
}
