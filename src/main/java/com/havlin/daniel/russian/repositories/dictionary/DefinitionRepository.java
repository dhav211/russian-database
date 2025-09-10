package com.havlin.daniel.russian.repositories.dictionary;

import com.havlin.daniel.russian.entities.dictionary.Definition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DefinitionRepository extends JpaRepository<Definition, Long> {
}
