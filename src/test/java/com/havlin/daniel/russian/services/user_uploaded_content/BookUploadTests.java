package com.havlin.daniel.russian.services.user_uploaded_content;

import com.havlin.daniel.russian.entities.user_uploaded_content.Book;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.Map;
import java.util.zip.ZipFile;

@SpringBootTest
public class BookUploadTests {
    private static final Logger log = LoggerFactory.getLogger(BookUploadTests.class);
    @Autowired
    private BookService bookService;

    @Test
    @Transactional
    public void testProcessEPUB() {
        try (ZipFile epub = new ZipFile("/Users/danielhavlin/Downloads/who-went-to-singapore/singapur.epub")) {
            Book book = bookService.createBook(epub);

            Assertions.assertAll(
                    () -> Assertions.assertEquals("Кто поедет в Сингапур?", book.getTitle())
            );
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
