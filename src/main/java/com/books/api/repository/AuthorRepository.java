package com.books.api.repository;

import com.books.api.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public interface AuthorRepository extends JpaRepository<Author, String> {

    @Override
    List<Author> findAll();
}
