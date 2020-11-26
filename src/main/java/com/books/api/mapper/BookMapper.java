package com.books.api.mapper;

import com.books.api.domain.Author;
import com.books.api.domain.Book;
import com.books.api.domain.BookDto;
import com.books.api.domain.Category;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class BookMapper {

    public Book mapToBook(final BookDto bookDto) {
        Set<Author> authors = bookDto.getAuthors().stream().map(a ->
                new Author(a, bookDto.rating)).collect(Collectors.toSet());
        Set<Category> categories = bookDto.getCategories().stream().map(Category::new).collect(Collectors.toSet());
        return new Book(
                bookDto.getIsbn(),
                bookDto.getTitle(),
                bookDto.getSubtitle(),
                bookDto.getPublisher(),
                bookDto.getPublishedDate(),
                bookDto.getDescription(),
                bookDto.getPageCount(),
                bookDto.getThumbnailUrl(),
                bookDto.getLanguage(),
                bookDto.getPreviewLink(),
                bookDto.getRating(),
                authors,
                categories);
    }

    public BookDto mapToBookDto(final Book book) {
        Set<String> authors = book.getAuthors().stream().map(Author::getName).collect(Collectors.toSet());
        Set<String> categories = book.getCategories().stream().map(Category::getName).collect(Collectors.toSet());
        return new BookDto(
                book.getId(),
                book.getTitle(),
                book.getSubtitle(),
                book.getPublisher(),
                book.getPublishedDate(),
                book.getDescription(),
                book.getPageCount(),
                book.getThumbnailUrl(),
                book.getLanguage(),
                book.getPreviewLink(),
                book.getRating(),
                authors,
                categories);
    }

    public List<BookDto> mapToBookDtoList(final List<Book> bookList) {
        return bookList.stream().map(this::mapToBookDto).collect(Collectors.toList());
    }

    public List<BookDto> mapToBookDtoList(final Set<Book> bookList) {
        return bookList.stream().map(this::mapToBookDto).collect(Collectors.toList());
    }
}
