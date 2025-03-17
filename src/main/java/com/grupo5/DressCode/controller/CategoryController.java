package com.grupo5.DressCode.controller;

import com.grupo5.DressCode.dto.CategoryDTO;
import com.grupo5.DressCode.service.ICategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/category")
public class CategoryController {
    private final ICategoryService categoryService;

    public CategoryController(ICategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<CategoryDTO> crearCategoria(@RequestBody CategoryDTO categoryDTO) {
        CategoryDTO categoriaARetornar = categoryService.createCategory(categoryDTO);
        if (categoriaARetornar == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else {
            return ResponseEntity.status(HttpStatus.CREATED).body(categoriaARetornar);
        }
    }

    @GetMapping
    public ResponseEntity<List<CategoryDTO>> traerTodos() {
        return ResponseEntity.ok(categoryService.searchAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> buscarCategoriaPorId(@PathVariable Integer id) {
        Optional<CategoryDTO> categoria = categoryService.searchForId(id);
        if (categoria.isPresent()) {
            return ResponseEntity.ok(categoria.get());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> actualizarCategoria(@PathVariable Integer id, @RequestBody CategoryDTO categoryDTO) {
        try {
            categoryService.updateCategory(id, categoryDTO);
            return ResponseEntity.ok("{\"message\": \"Categoria modificada\"}");
        } catch (RuntimeException e) {
            return new ResponseEntity<>("{\"message\": \"" + e.getMessage() + "\"}", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> borrarCategoria(@PathVariable Integer id) {
        Optional<CategoryDTO> categoriaOptional = categoryService.searchForId(id);
        if (categoriaOptional.isPresent()) {
            categoryService.deleteCategory(id);
            return ResponseEntity.ok("{\"message\": \"categoria eliminada\"}");
        } else {
            return new ResponseEntity<>("{\"message\": \"categoria no encontrada\"}", HttpStatus.NOT_FOUND);
        }
    }
}
