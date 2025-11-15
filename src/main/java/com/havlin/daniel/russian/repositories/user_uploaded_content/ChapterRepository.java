package com.havlin.daniel.russian.repositories.user_uploaded_content;

import com.havlin.daniel.russian.entities.user_uploaded_content.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChapterRepository extends JpaRepository<Chapter, Long> {
}
