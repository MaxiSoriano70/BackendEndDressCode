package com.grupo5.DressCode.controller;

import com.grupo5.DressCode.entity.Color;
import com.grupo5.DressCode.service.IColorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/color")
public class ColorController {
    public IColorService colorService;

    public ColorController(IColorService colorService) {
        this.colorService = colorService;
    }

    @PostMapping
    public ResponseEntity<Color> crearColor(@RequestBody Color color){
        Color colorARetornar = colorService.createColor(color);
        if(colorARetornar == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }else {
            return ResponseEntity.status(HttpStatus.CREATED).body(colorARetornar);
        }
    }
    @GetMapping
    public ResponseEntity<List<Color>> traerTodos(){
        return ResponseEntity.ok(colorService.searchAll());
    }
    @GetMapping("/{id}")
    public ResponseEntity<Color> buscarColorPorId(@PathVariable Integer id){
        Optional<Color> color = colorService.searchForId(id);
        if(color.isPresent()){
            Color colorARetornar = color.get();
            return ResponseEntity.ok(colorARetornar);
        }
        else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    @PutMapping
    public ResponseEntity<String> actualizarColor(@RequestBody Color color){
        Optional<Color> colorOptional = colorService.searchForId(color.getColorId());
        if (colorOptional.isPresent()) {
            colorService.updateColor(color);
            return ResponseEntity.ok("{\"message\": \"color modificado\"}");
        } else {
            return new ResponseEntity<>("{\"message\": \"color no encontrado\"}", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> borrarCategoria(@PathVariable Integer id){
        Optional<Color> colorOptional = colorService.searchForId(id);
        if (colorOptional.isPresent()) {
            colorService.deleteColor(id);
            return ResponseEntity.ok("{\"message\": \"color eliminado\"}");
        } else {
            return new ResponseEntity<>("{\"message\": \"color no encontrado\"}", HttpStatus.NOT_FOUND);
        }
    }
}
