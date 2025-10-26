package com.havlin.daniel.russian.repositories.generated_content;

import com.havlin.daniel.russian.entities.generated_content.Definition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DefinitionRepository extends JpaRepository<Definition, Long> {
    List<Definition> findAllByWordId(Long id);
}
