package com.amerkhaled.blogplatform.services.impl;

import com.amerkhaled.blogplatform.domain.entities.Category;
import com.amerkhaled.blogplatform.repositories.CategoryRepository;
import com.amerkhaled.blogplatform.services.CategoryService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<Category> listCategories() {
        log.info("Fetching all categories with post counts");
        List<Category> categories = categoryRepository.findAllWithPostCount();
        log.debug("Retrieved {} categories", categories.size());
        return categories;
    }

    @Override
    @Transactional
    public Category createCategory(Category category) {
        log.info("Attempting to create new category with name '{}'", category.getName());

        if (categoryRepository.existsByNameIgnoreCase(category.getName())) {
            log.warn("Category creation failed — category with name '{}' already exists", category.getName());
            throw new IllegalArgumentException("Category with name " + category.getName() + " already exists");
        }

        Category savedCategory = categoryRepository.save(category);
        log.info("Category '{}' created successfully with ID {}", savedCategory.getName(), savedCategory.getId());
        return savedCategory;
    }

    @Override
    @Transactional
    public void deleteCategory(UUID id) {
        log.info("Attempting to delete category with ID {}", id);

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Category deletion failed — category with ID {} does not exist", id);
                    return new IllegalArgumentException("Category with id " + id + " does not exist");
                });

        if (!category.getPosts().isEmpty()) {
            log.warn("Category deletion blocked — category with ID {} has {} associated posts", id, category.getPosts().size());
            throw new IllegalStateException("Category with id " + id + " cannot be deleted, it has associated posts");
        }

        categoryRepository.delete(category);
        log.info("Category with ID {} deleted successfully", id);
    }

    @Override
    public Category getCategoryById(UUID id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category with id " + id + " does not exist"));
    }
}
