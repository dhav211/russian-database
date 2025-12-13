package com.havlin.daniel.russian.repositories.generated_content;

import com.havlin.daniel.russian.entities.generated_content.Sentence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SentenceRepository extends JpaRepository<Sentence, Long> {
    //List<Sentence> findAllByWordId(Long id);
    List<Sentence> findAllByText(String text);

    @Query("SELECT s FROM Sentence s WHERE s.text IN " +
            "(SELECT s2.text FROM Sentence s2 GROUP BY s2.text HAVING COUNT(*) > 1)")
    List<Sentence> findDuplicateSentences();
}
