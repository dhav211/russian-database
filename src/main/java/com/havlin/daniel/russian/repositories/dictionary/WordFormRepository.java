package com.havlin.daniel.russian.repositories.dictionary;

import com.havlin.daniel.russian.entities.dictionary.WordForm;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WordFormRepository extends JpaRepository<WordForm, Long> {
}
