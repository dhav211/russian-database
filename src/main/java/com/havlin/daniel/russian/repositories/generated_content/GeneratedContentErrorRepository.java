package com.havlin.daniel.russian.repositories.generated_content;

import com.havlin.daniel.russian.entities.generated_content.GeneratedContentError;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GeneratedContentErrorRepository extends JpaRepository<GeneratedContentError, Long> {
    @Query("SELECT e FROM GeneratedContentError e WHERE e.originatingEntityId = :id")
    List<GeneratedContentError> findAllByOriginatingEntityId(Long id);
}
