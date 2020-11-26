package com.books.api.mapper;

import com.books.api.domain.Author;
import com.books.api.domain.AuthorDto;
import com.books.api.domain.Book;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class AuthorMapper {

    public AuthorDto mapToAuthorDto(Author author) {
        Set<String> bookIds = author.getBooksList().stream().map(Book::getId).collect(Collectors.toSet());
        return new AuthorDto(author.getName(), author.getAverageRating(), bookIds);
    }

    public List<AuthorDto> mapToAuthorDtoList(List<Author> authors) {
        return authors.stream().map(this::mapToAuthorDto).collect(Collectors.toList());
    }
}
