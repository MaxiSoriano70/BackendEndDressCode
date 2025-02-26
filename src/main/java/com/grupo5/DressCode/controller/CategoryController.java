package com.grupo5.DressCode.controller;

import com.grupo5.DressCode.entity.Category;
import com.grupo5.DressCode.service.ICategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/category")
public class CategoryController {
    public ICategoryService categoryService;

    public CategoryController(ICategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<Category> crearCategoria(@RequestBody Category category){
        Category categoriaARetornar = categoryService.createCategory(category);
        if(categoriaARetornar == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }else {
            return ResponseEntity.status(HttpStatus.CREATED).body(categoriaARetornar);
        }
    }
    @GetMapping
    public ResponseEntity<List<Category>> traerTodos(){
        return ResponseEntity.ok(categoryService.searchAll());
    }
    @GetMapping("/{id}")
    public ResponseEntity<Category> buscarCategoriaPorId(@PathVariable Integer id){
        Optional<Category> categoria = categoryService.searchForId(id);
        if(categoria.isPresent()){
            Category categoriaARetornar = categoria.get();
            return ResponseEntity.ok(categoriaARetornar);
        }
        else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    @PutMapping
    public ResponseEntity<String> actualizarCategoria(@RequestBody Category category){
        Optional<Category> categoriaOptional = categoryService.searchForId(category.getCategoryId());
        if (categoriaOptional.isPresent()) {
            categoryService.updateCategory(category);
            return ResponseEntity.ok("{\"message\": \"categoria modificada\"}");
        } else {
            return new ResponseEntity<>("{\"message\": \"categoria no encontrada\"}", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> borrarCategoria(@PathVariable Integer id){
        Optional<Category> categoriaOptional = categoryService.searchForId(id);
        if (categoriaOptional.isPresent()) {
            categoryService.deleteCategory(id);
            return ResponseEntity.ok("{\"message\": \"categoria eliminada\"}");
        } else {
            return new ResponseEntity<>("{\"message\": \"categoria no encontrada\"}", HttpStatus.NOT_FOUND);
        }
    }
}
