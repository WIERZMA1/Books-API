package com.books.api.repository;

import com.books.api.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public interface BookRepository extends JpaRepository<Book, String> {

    @Override
    List<Book> findAll();
}
