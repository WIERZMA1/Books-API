package com.books.api.controller;

import com.books.api.domain.BookDto;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
class RestControllerTestSuite {

    @Autowired
    RestController restController;

    @Test
    void getBooks() {
    }

    @Test
    void getBook() {
    }

    @Test
    void searchBook() {
    }

    @Test
    void getBooksByCategory() {
    }

    @Test
    void deleteBook() {
    }

    @Test
    void updateBook() {
    }

    @Test
    void createBook() throws BookNotFoundException {
        //Given
        List<String> authors = new ArrayList<>(Arrays.asList("author", "other author"));
        List<String> categories = new ArrayList<>(Arrays.asList("category", "other category"));
        BookDto book = new BookDto("id", "title", "subtitle", "", 20200910L, "description",
                666, "", "PL", "", 3.3, authors, categories);
        // When
        restController.createBook(book);
        // Then
        String title = book.getTitle();
        String savedTitle = restController.getBook("id").getTitle();
        Assert.assertEquals(title, savedTitle);
    }
}