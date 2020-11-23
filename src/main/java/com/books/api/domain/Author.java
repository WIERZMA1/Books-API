package com.books.api.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "author")
public class Author {

    @Id
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "authors", targetEntity = Book.class)
    @Column(name = "books")
    private List<Book> booksList = new ArrayList<>();

    @Column(name = "rating")
    private double averageRating;

    public Author(String name, double rating) {
        this.name = name;
        DecimalFormat df = new DecimalFormat("#.##");
        rating = Double.isNaN(rating) ? 0.0 : rating;
        averageRating = averageRating == 0.0 && rating != 0.0 ? rating : Double.parseDouble(df.format(averageRating + rating / 2));
    }
}
