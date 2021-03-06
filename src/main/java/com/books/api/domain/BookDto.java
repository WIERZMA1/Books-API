package com.books.api.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {

    public String isbn;
    public String title;
    public String subtitle;
    public String publisher;
    public long publishedDate;
    public String description;
    public int pageCount;
    public String thumbnailUrl;
    public String language;
    public String previewLink;
    public double rating;
    public Set<String> authors;
    public Set<String> categories;

}
