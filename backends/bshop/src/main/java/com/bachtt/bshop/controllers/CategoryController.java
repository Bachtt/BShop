package com.bachtt.bshop.controllers;

import com.bachtt.bshop.dto.response.ResponseMessage;
import com.bachtt.bshop.models.Category;
import com.bachtt.bshop.models.User;
import com.bachtt.bshop.security.userpincal.UserDetailService;
import com.bachtt.bshop.services.impl.CategoryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = "*")
@RequestMapping("category")
@RestController
public class CategoryController {
    @Autowired
    CategoryServiceImpl categoryService;
    @Autowired
    UserDetailService userDetailService;

    @GetMapping
    public ResponseEntity<?> pageCategory(@PageableDefault(sort = "nameCategory", direction = Sort.Direction.ASC)Pageable pageable){
        Page<Category> categoryPage = categoryService.findAll(pageable);
        if(categoryPage.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(categoryPage, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createCategory(@RequestBody Category category){
        User user = userDetailService.getCurrentUser();
        if(!user.getUsername().equals("Anonymous")){
            if (categoryService.existsByNameCategory(category.getNameCategory())){
                return new ResponseEntity<>(new ResponseMessage("no_name_category"), HttpStatus.OK);
            }
            categoryService.save(category);
            return new ResponseEntity<>(new ResponseMessage("yes"), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ResponseMessage("create failed"), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id){
        Optional<Category> category = categoryService.findById(id);
        if(!category.isPresent()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        categoryService.deleteById(category.get().getId());
        return new ResponseEntity<>(new ResponseMessage("Delete successfully!"), HttpStatus.OK);
    }

    //Cach 1: dung @PathVariable
//    @GetMapping("/search/{nameCategory}")
//    public ResponseEntity<?> searchByNameCategory(@PathVariable String nameCategory, @PageableDefault(sort = "nameCategory", direction = Sort.Direction.ASC)Pageable pageable){
//        Page<Category> categoryPage = categoryService.findByNameCategoryContaining(nameCategory, pageable);
//        if(categoryPage.isEmpty()){
//            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//        }
//        return new ResponseEntity<>(categoryPage, HttpStatus.OK);
//    }
    //Cach 2: dung @RequestParams
    @GetMapping("/search/")
    public ResponseEntity<?> searchByNameCategory(@RequestParam("nameCategory") String nameCategory, @PageableDefault(sort = "nameCategory", direction = Sort.Direction.ASC)Pageable pageable){
        Page<Category> categoryPage = categoryService.findByNameCategoryQuery(nameCategory, pageable);
        if(categoryPage.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(categoryPage, HttpStatus.OK);
    }
}
