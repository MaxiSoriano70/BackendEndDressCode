package com.grupo5.DressCode.service;

import com.grupo5.DressCode.dto.CategoryDTO;
import com.grupo5.DressCode.entity.Category;

import java.util.List;
import java.util.Optional;

public interface ICategoryService {
    CategoryDTO createCategory(CategoryDTO categoryDTO);
    Optional<CategoryDTO> searchForId(int id);
    List<CategoryDTO> searchAll();
    void updateCategory(int id, CategoryDTO categoryDTO);
    void deleteCategory(Integer id);
}
