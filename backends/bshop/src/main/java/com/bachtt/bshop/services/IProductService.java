package com.bachtt.bshop.services;

import com.bachtt.bshop.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface IProductService {
    Page<Product> findAll(Pageable pageable);
    Product save(Product product);
    Optional<Product> findById(Long id);
    void deleteById(Long id);
}
