package com.esliceu.forum.controllers;

import com.esliceu.forum.models.Category;
import com.esliceu.forum.services.CategoryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class CategoriesController {

    @Autowired
    CategoryServiceImpl categoryService;

    @GetMapping("/categories")
    public List<Category> getCategories() {
        return categoryService.findAll();
    }

    @PreAuthorize("hasAnyRole('User','Moderator','Admin')")
    @PostMapping("/categories")
    public Map<String,Object> newCategoria(@RequestBody Map<String,Object> data){
        String title = (String) data.get("title");
        if (title.contains("/")){
            data.put("title",title.replace("/",""));
        }
        data.put("slug",title);
        Category c = new Category();
        c.setSlug((String) data.get("title"));
        c.setTitle((String) data.get("title"));
        c.setDescription((String) data.get("description"));
        categoryService.newCategory(c);
        return data;
    }

    @PreAuthorize("hasAnyRole('User','Moderator','Admin')")
    @PutMapping("/categories/{title}")
    public Map<String,Object> updateCategory(@PathVariable String title,@RequestBody Map<String,Object> params){
        Category category = categoryService.getByName(title);
        category.setTitle((String) params.get("title"));
        category.setSlug((String) params.get("title"));
        category.setDescription((String) params.get("description"));
        categoryService.newCategory(category);
        return params;
    }

    @PreAuthorize("hasAnyRole('Moderator','Admin')")
    @DeleteMapping("/categories/{title}")
    public String deleteCategory(@PathVariable String title){
        categoryService.deleteCategoryByTitle(title);
        return "ok";
    }

}
