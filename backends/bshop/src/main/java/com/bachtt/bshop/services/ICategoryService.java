package com.bachtt.bshop.services;

import com.bachtt.bshop.models.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ICategoryService {
    List<Category> findAll();
    Page<Category> findAll(Pageable pageable);
    Category save(Category category);
    Boolean existsByNameCategory(String nameCategory);
    void deleteById(Long id);
    Optional<Category> findById(Long id);
    Page<Category> findByNameCategoryContaining(String nameCategory, Pageable pageable);
    Page<Category> findByNameCategoryQuery(@Param("nameCategory") String nameCategory, Pageable pageable);
}
