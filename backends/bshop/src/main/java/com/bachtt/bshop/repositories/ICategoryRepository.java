package com.bachtt.bshop.repositories;

import com.bachtt.bshop.models.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ICategoryRepository extends JpaRepository<Category, Long> {
    Boolean existsByNameCategory(String nameCategory);
    //C1: dung ham co san cua repository
    Page<Category> findByNameCategoryContaining(String nameCategory, Pageable pageable);

    //C2: tu trien khai @query
    @Query("SELECT c from Category AS c where c.nameCategory like concat('%', :nameCategory, '%') ")
    Page<Category> findByNameCategoryQuery(@Param("nameCategory") String nameCategory, Pageable pageable);
}
