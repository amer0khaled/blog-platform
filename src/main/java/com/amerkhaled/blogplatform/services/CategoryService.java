package com.amerkhaled.blogplatform.services;

import com.amerkhaled.blogplatform.domain.entities.Category;

import java.util.List;
import java.util.UUID;

public interface CategoryService {
    List<Category> listCategories();
    Category createCategory(Category categor);
    void deleteCategory(UUID id);
    Category getCategoryById(UUID id);

}
