package com.books.api.service;

import com.books.api.domain.Author;
import com.books.api.domain.Book;
import com.books.api.domain.Category;
import com.books.api.repository.AuthorRepository;
import com.books.api.repository.BookRepository;
import com.books.api.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class DbService {

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Optional<Book> getBook(final String id) {
        return bookRepository.findById(id);
    }

    public Set<Book> searchBookContaining(final String str) {
        return getAllBooks().stream()
                .filter(b -> b.getTitle().toLowerCase().contains(str.toLowerCase())
                        || b.getSubtitle().toLowerCase().contains(str.toLowerCase()))
                .collect(Collectors.toSet());
    }

    public Set<Book> getAllBooksByCategory(final String category) {
        return categoryRepository.findById(category).isPresent() ?
                categoryRepository.findById(category).get().getBooksList() : new HashSet<>();
    }

    public Set<Book> getAllBooksByAuthor(final String author) {
        return authorRepository.findById(author).isPresent() ?
                authorRepository.findById(author).get().getBooksList() : new HashSet<>();
    }

    public Book saveBook(final Book book) {
        return bookRepository.save(book);
    }

    public void deleteBook(final String isbn) {
        bookRepository.deleteById(isbn);
    }

    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    public Optional<Author> getAuthor(String name) {
        return authorRepository.findById(name);
    }

    public List<Author> searchAuthorContaining(final String str) {
        return getAllAuthors().stream()
                .filter(a -> a.getName().toLowerCase().contains(str.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
}