package com.amerkhaled.blogplatform.controllers;

import com.amerkhaled.blogplatform.domain.dtos.CategoryDto;
import com.amerkhaled.blogplatform.domain.dtos.CreateCategoryRequestDto;
import com.amerkhaled.blogplatform.domain.entities.Category;
import com.amerkhaled.blogplatform.mappers.CategoryMapper;
import com.amerkhaled.blogplatform.services.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    @GetMapping
    public ResponseEntity<List<CategoryDto>> listCategories() {
        List<CategoryDto> categoryDtos = categoryService.listCategories().stream()
                .map(categoryMapper::toDto)
                .toList();
        return ResponseEntity.ok(categoryDtos);
    }

    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CreateCategoryRequestDto categoryRequest) {
        Category categoryToCreate = categoryMapper.toEntity(categoryRequest);
        Category savedCategory = categoryService.createCategory(categoryToCreate);
        CategoryDto categoryDto = categoryMapper.toDto(savedCategory);
        return new ResponseEntity<>(categoryDto, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable UUID id) {
        categoryService.deleteCategory(id);
        return new ResponseEntity<> (HttpStatus.NO_CONTENT);
    }

}
