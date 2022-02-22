package com.esliceu.forum.services;

import com.esliceu.forum.models.Category;

import java.util.List;

public interface CategoryService {
    void newCategory(Category c);

    List<Category> findAll();

    Category getByName(String category);

    void deleteCategoryByTitle(String title);
}
