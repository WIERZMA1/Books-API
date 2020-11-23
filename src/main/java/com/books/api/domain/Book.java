package com.books.api.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "book")
public class Book {

    @Id
    @Column(name = "ISBN", nullable = false, unique = true)
    private String id;

    @Column(name = "title")
    private String title;

    @Column(name = "subtitle")
    private String subtitle;

    @Column(name = "publisher")
    private String publisher;

    @Column(name = "publishedDate")
    private long publishedDate;

    @Column(name = "description", columnDefinition = "LONGVARCHAR")
    private String description;

    @Column(name = "pageCount")
    private int pageCount;

    @Column(name = "thumbnailURL")
    private String thumbnailUrl;

    @Column(name = "language")
    private String language;

    @Column(name = "previewLink")
    private String previewLink;

    @Column(name = "rating")
    private double rating;

    @Fetch(FetchMode.SELECT)
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, targetEntity = Author.class)
    @JoinTable(
            name = "books_authors",
            joinColumns = @JoinColumn(name = "author_name"),
            inverseJoinColumns = @JoinColumn(name = "book_id")
    )
    private List<Author> authors;

    @Fetch(FetchMode.SELECT)
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, targetEntity = Category.class)
    @JoinTable(
            name = "books_categories",
            joinColumns = @JoinColumn(name = "category_name"),
            inverseJoinColumns = @JoinColumn(name = "book_id")
    )
    private List<Category> categories;
}
