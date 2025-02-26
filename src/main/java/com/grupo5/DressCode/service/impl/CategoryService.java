package com.grupo5.DressCode.service.impl;

import com.grupo5.DressCode.entity.Category;
import com.grupo5.DressCode.repository.ICategoryRepository;
import com.grupo5.DressCode.service.ICategoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService implements ICategoryService {
    private ICategoryRepository categoryRepository;

    public CategoryService(ICategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public Optional<Category> searchForId(int id) {
        return categoryRepository.findById(id);
    }

    @Override
    public List<Category> searchAll() {
        return categoryRepository.findAll();
    }

    @Override
    public void updateCategory(Category category) {
        categoryRepository.save(category);
    }

    @Override
    public void deleteCategory(Integer id) {
        categoryRepository.deleteById(id);
    }
}