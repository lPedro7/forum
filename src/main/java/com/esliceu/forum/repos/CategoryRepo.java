package com.esliceu.forum.repos;

import com.esliceu.forum.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepo extends JpaRepository<Category,Integer> {

    Category findAllByTitle(String category);
}
