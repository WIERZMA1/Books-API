package com.books.api.repository;

import com.books.api.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {

    @Override
    List<Category> findAll();
}
