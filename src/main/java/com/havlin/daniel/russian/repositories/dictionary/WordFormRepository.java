package com.havlin.daniel.russian.repositories.dictionary;

import com.havlin.daniel.russian.entities.dictionary.Word;
import com.havlin.daniel.russian.entities.dictionary.WordForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface WordFormRepository extends JpaRepository<WordForm, Long> {
    List<WordForm> findAllByAccented(String accentedText);
    List<WordForm> findDistinctWordIdByAccented(String accented);
    List<WordForm> findAllByBare(String bareText);
    List<WordForm> getAllByWordId(Long id);
    boolean existsByAccented(String accented);
}
