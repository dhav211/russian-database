package com.havlin.daniel.russian.services.user_uploaded_content;

import com.havlin.daniel.russian.entities.user_uploaded_content.Book;
import com.havlin.daniel.russian.entities.user_uploaded_content.Chapter;
import com.havlin.daniel.russian.entities.user_uploaded_content.ChapterText;
import com.havlin.daniel.russian.repositories.user_uploaded_content.BookRepository;
import com.havlin.daniel.russian.repositories.user_uploaded_content.ChapterTextRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.zip.ZipFile;

@SpringBootTest
public class BookUploadTests {
    private static final Logger log = LoggerFactory.getLogger(BookUploadTests.class);
    @Autowired
    private BookService bookService;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private ChapterTextRepository chapterTextRepository;

    @Test
    @Transactional
    public void testProcessEPUB() {
        try (ZipFile epub = new ZipFile("./singapur.epub")) {
            CreatedBook createdBook = bookService.createBook(epub);
            Book retrievedBook = bookRepository.findBookByTitle(createdBook.book().getTitle());
            Chapter chapter2 = retrievedBook.getChapterByNumber(2);
            Chapter chapter6 = retrievedBook.getChapterByNumber(6);
            ChapterText chapter2Text = chapterTextRepository.findByChapterId(chapter2.getId());
            ChapterText chapter6Text = chapterTextRepository.findByChapterId(chapter6.getId());

            Assertions.assertAll(
                    () -> Assertions.assertEquals("Кто поедет в Сингапур?", retrievedBook.getTitle()),
                    () -> Assertions.assertNotNull(retrievedBook),
                    () -> Assertions.assertTrue(chapter2Text.getText().contains("Никакой дисциплины. Совсем разболтались! Ну, я вас научу учиться. Ну, я вам покажу, что такое порядок!")),
                    () -> Assertions.assertTrue(chapter6Text.getText().contains("Я обернулась – ну конечно же, это был он, популярный светский репортер Лева!")),
                    () -> Assertions.assertEquals("", createdBook.possibleErrorMessage())
            );
        } catch (IOException e) {
            log.error(e.getMessage());
        }

        try(ZipFile epub = new ZipFile("./catcherbook.epub")) {
            CreatedBook createdBook = bookService.createBook(epub);
            Book retrievedBook = bookRepository.findBookByTitle(createdBook.book().getTitle());

            Assertions.assertAll(
                    () -> Assertions.assertEquals("ePUB file language not detected as Russian", createdBook.possibleErrorMessage()),
                    () -> Assertions.assertNull(retrievedBook)
            );
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
