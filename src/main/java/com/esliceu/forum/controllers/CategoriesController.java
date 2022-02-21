package com.esliceu.forum.controllers;

import com.esliceu.forum.models.Category;
import com.esliceu.forum.services.CategoryService;
import com.esliceu.forum.services.CategoryServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class CategoriesController {

    @Autowired
    CategoryServiceImpl categoryService;

    @GetMapping("/categories")
    public List<Category> getCategories() {

        List<Category> categories = categoryService.findAll();

        return categories;
    }

    @PostMapping("/categories")
    public String newCategoria(@RequestBody String data) throws JsonProcessingException {

        Category c = new Gson().fromJson(data, Category.class);

        if (c.getTitle().contains("/")){
            c.setTitle(c.getTitle().replace("/",""));
            c.setSlug(c.getTitle());
        }
        c.setSlug(c.getTitle());
        categoryService.newCategory(c);
        return new Gson().toJson(c);

    }

}
