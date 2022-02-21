package com.esliceu.forum.services;

import com.esliceu.forum.models.Category;
import com.esliceu.forum.repos.CategoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService{

    @Autowired
    CategoryRepo categoryRepo;

    @Override
    public void newCategory(Category c) {
        categoryRepo.save(c);
    }

    public List<Category> findAll() {
        return categoryRepo.findAll();
    }

    @Override
    public Category getByName(String category) {
        return categoryRepo.findAllByTitle(category);
    }
}
