package com.bachtt.bshop.services;

import com.bachtt.bshop.models.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ICategoryService {
    Page<Category> findAll(Pageable pageable);
    Category save(Category category);
    Boolean existsByNameCategory(String nameCategory);
}
