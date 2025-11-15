package com.havlin.daniel.russian.repositories.user_uploaded_content;

import com.havlin.daniel.russian.entities.user_uploaded_content.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
    Book findBookByTitle(String title);
}
