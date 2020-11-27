package com.books.api.controller;

import com.books.api.domain.AuthorDto;
import com.books.api.domain.BookDto;
import com.books.api.domain.CategoryDto;
import com.books.api.mapper.AuthorMapper;
import com.books.api.mapper.BookMapper;
import com.books.api.mapper.CategoryMapper;
import com.books.api.service.DbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@CrossOrigin(origins = "*")
@org.springframework.web.bind.annotation.RestController
@RequestMapping("/api")
public class RestController {

    @Autowired
    private DbService service;
    @Autowired
    private BookMapper bookMapper;
    @Autowired
    private AuthorMapper authorMapper;
    @Autowired
    private CategoryMapper categoryMapper;

    @RequestMapping(method = RequestMethod.GET, value = "/books")
    public List<BookDto> getBooks() {
        return bookMapper.mapToBookDtoList(service.getAllBooks());
    }

    @RequestMapping(method = RequestMethod.GET, value = "/book/{isbn}")
    public BookDto getBook(@PathVariable("isbn") String isbn) throws BookNotFoundException {
        return bookMapper.mapToBookDto(service.getBook(isbn).orElseThrow(BookNotFoundException::new));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/book/search")
    public List<BookDto> searchBook(@RequestParam String str) {
        return bookMapper.mapToBookDtoList(service.searchBookContaining(str));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/category/{categoryName}/books")
    public List<BookDto> getBooksByCategory(@PathVariable String categoryName) {
        return bookMapper.mapToBookDtoList(service.getAllBooksByCategory(categoryName));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/author/{authorName}/books")
    public List<BookDto> getBooksByAuthor(@PathVariable String authorName) {
        return bookMapper.mapToBookDtoList(service.getAllBooksByAuthor(authorName));
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/book/{isbn}")
    public void deleteBook(@PathVariable("isbn") String isbn) {
        service.deleteBook(isbn);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/book", consumes = APPLICATION_JSON_VALUE)
    public BookDto updateBook(@RequestBody BookDto bookDto) {
        return bookMapper.mapToBookDto(service.saveBook(bookMapper.mapToBook(bookDto)));
    }

    @RequestMapping(method = RequestMethod.POST, value = "/book", consumes = APPLICATION_JSON_VALUE)
    public void createBook(@RequestBody BookDto bookDto) {
        service.saveBook(bookMapper.mapToBook(bookDto));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/authors")
    public List<AuthorDto> getAuthors() {
        return authorMapper.mapToAuthorDtoList(service.getAllAuthors());
    }

    @RequestMapping(method = RequestMethod.GET, value = "/author/search")
    public List<AuthorDto> searchAuthor(@RequestParam String str) {
        return authorMapper.mapToAuthorDtoList(service.searchAuthorContaining(str));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/categories")
    public List<CategoryDto> getAllCategories() {
        return categoryMapper.mapToCategoryDtoList(service.getAllCategories());
    }
}