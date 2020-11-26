package com.books.api.mapper;

import com.books.api.domain.Book;
import com.books.api.domain.Category;
import com.books.api.domain.CategoryDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class CategoryMapper {

    public CategoryDto mapToCategoryDto(Category category) {
        Set<String> bookIds = category.getBooksList().stream().map(Book::getId).collect(Collectors.toSet());
        return new CategoryDto(category.getName(), bookIds);
    }

    public List<CategoryDto> mapToCategoryDtoList(List<Category> categories) {
        return categories.stream().map(this::mapToCategoryDto).collect(Collectors.toList());
    }
}
