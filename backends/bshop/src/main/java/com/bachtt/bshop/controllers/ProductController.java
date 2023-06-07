package com.bachtt.bshop.controllers;

import com.bachtt.bshop.dto.response.ResponseMessage;
import com.bachtt.bshop.models.Product;
import com.bachtt.bshop.services.impl.ProductServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("product")
public class ProductController {
    @Autowired
    ProductServiceImpl productService;

    @GetMapping
    public ResponseEntity<?> pageProduct(@PageableDefault(sort = "nameProduct", direction = Sort.Direction.ASC)Pageable pageable){
        Page<Product> productPage = productService.findAll(pageable);
        if(productPage.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(productPage, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody Product product){
        if(product.getNameProduct()==null){
            return new ResponseEntity<>(new ResponseMessage("no_name_product"), HttpStatus.OK);
        }
        productService.save(product);
        return new ResponseEntity<>(new ResponseMessage("yes"), HttpStatus.OK);
    }
}
