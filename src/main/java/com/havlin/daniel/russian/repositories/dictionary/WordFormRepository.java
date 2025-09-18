package com.havlin.daniel.russian.repositories.dictionary;

import com.havlin.daniel.russian.entities.dictionary.WordForm;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WordFormRepository extends JpaRepository<WordForm, Long> {
    List<WordForm> findAllByAccented(String accentedText);
    List<WordForm> findAllByBare(String bareText);
    List<WordForm> getAllByWordId(Long id);
    boolean existsByAccented(String accented);
}
