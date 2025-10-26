package com.havlin.daniel.russian.repositories.generated_content;

import com.havlin.daniel.russian.entities.generated_content.Sentence;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SentenceRepository extends JpaRepository<Sentence, Long> {
    List<Sentence> findAllByWordId(Long id);
}
