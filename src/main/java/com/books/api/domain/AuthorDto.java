package com.books.api.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AuthorDto {

    public String name;
    public double rating;
    public Set<String> bookIds;

}
