package com.havlin.daniel.russian.services.user_uploaded_content;

import com.havlin.daniel.russian.entities.user_uploaded_content.Book;

public record CreatedBook(Book book, String possibleErrorMessage) { }
