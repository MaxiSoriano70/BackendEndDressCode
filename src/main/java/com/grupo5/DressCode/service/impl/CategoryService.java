package com.grupo5.DressCode.service.impl;

import com.grupo5.DressCode.dto.CategoryDTO;
import com.grupo5.DressCode.entity.Category;
import com.grupo5.DressCode.entity.Clothe;
import com.grupo5.DressCode.repository.ICategoryRepository;
import com.grupo5.DressCode.repository.IClotheRepository;
import com.grupo5.DressCode.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService implements ICategoryService {
    private final ICategoryRepository categoryRepository;
    @Autowired
    private IClotheRepository clotheRepository;

    public CategoryService(ICategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Category category = new Category();
        category.setName(categoryDTO.getName());
        category.setDescription(categoryDTO.getDescription());
        category.setCategoryImagenUrl(categoryDTO.getCategoryImagenUrl());

        Category savedCategory = categoryRepository.save(category);

        return convertToDTO(savedCategory);
    }

    @Override
    public Optional<CategoryDTO> searchForId(int id) {
        return categoryRepository.findById(id).map(this::convertToDTO);
    }

    @Override
    public List<CategoryDTO> searchAll() {
        return categoryRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void updateCategory(int id, CategoryDTO categoryDTO) {
        Optional<Category> categoryOpt = categoryRepository.findById(id);
        if (categoryOpt.isEmpty()) {
            throw new RuntimeException("Categoría no encontrada");
        }

        Category category = categoryOpt.get();

        if (categoryDTO.getName() != null && !categoryDTO.getName().isEmpty()) {
            category.setName(categoryDTO.getName());
        }
        if (categoryDTO.getDescription() != null && !categoryDTO.getDescription().isEmpty()) {
            category.setDescription(categoryDTO.getDescription());
        }
        if (categoryDTO.getCategoryImagenUrl() != null && !categoryDTO.getCategoryImagenUrl().isEmpty()) {
            category.setCategoryImagenUrl(categoryDTO.getCategoryImagenUrl());
        }

        categoryRepository.save(category);
    }

    @Override
    public void deleteCategory(Integer id) {
        // Buscar la categoría a eliminar
        Optional<Category> categoryOpt = categoryRepository.findById(id);
        if (categoryOpt.isEmpty()) {
            throw new RuntimeException("Categoría no encontrada");
        }
        Category category = categoryOpt.get();
        // Buscar todas las prendas asociadas con esta categoría
        List<Clothe> clothesWithCategory = clotheRepository.findByCategory(category);
        // Marcar todas las prendas asociadas como eliminadas lógicamente
        for (Clothe clothe : clothesWithCategory) {
            clothe.setCategory(null);
            clothe.deleteLogically();
        }
        // Guardar los cambios en las prendas
        clotheRepository.saveAll(clothesWithCategory);
        // Eliminar la categoría
        categoryRepository.delete(category);
    }

    private CategoryDTO convertToDTO(Category category) {
        return new CategoryDTO(
                category.getCategoryId(),
                category.getName(),
                category.getDescription(),
                category.getCategoryImagenUrl()
        );
    }
}