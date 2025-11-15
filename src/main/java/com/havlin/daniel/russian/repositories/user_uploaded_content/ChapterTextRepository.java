package com.havlin.daniel.russian.repositories.user_uploaded_content;

import com.havlin.daniel.russian.entities.user_uploaded_content.ChapterText;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChapterTextRepository extends JpaRepository<ChapterText, Long> {
    ChapterText findByChapterId(Long chapterId);
}
