package com.bachtt.bshop.services.impl;

import com.bachtt.bshop.models.Product;
import com.bachtt.bshop.models.User;
import com.bachtt.bshop.repositories.IProductRepository;
import com.bachtt.bshop.security.userpincal.UserDetailService;
import com.bachtt.bshop.services.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductServiceImpl implements IProductService {
    @Autowired
    IProductRepository productRepository;
    @Autowired
    UserDetailService userDetailService;
    @Override
    public Page<Product> findAll(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Override
    public Product save(Product product) {
        User user = userDetailService.getCurrentUser();
        product.setUser(user);
        return productRepository.save(product);
    }

    @Override
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }
}
