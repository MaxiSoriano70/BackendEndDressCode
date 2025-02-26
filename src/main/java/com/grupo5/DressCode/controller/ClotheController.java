package com.grupo5.DressCode.controller;

import com.grupo5.DressCode.entity.Clothe;
import com.grupo5.DressCode.service.IClotheService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/clothe")
public class ClotheController {
    public IClotheService clotheService;

    public ClotheController(IClotheService clotheService) {
        this.clotheService = clotheService;
    }
    @PostMapping
    public ResponseEntity<Clothe> crearPrenda(@RequestBody Clothe clothe){
        Clothe prendaARetornar = clotheService.createClothe(clothe);
        if(prendaARetornar == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }else {
            return ResponseEntity.status(HttpStatus.CREATED).body(prendaARetornar);
        }
    }
    @GetMapping
    public ResponseEntity<List<Clothe>> traerTodos(){
        return ResponseEntity.ok(clotheService.searchAll());
    }
    @GetMapping("/{id}")
    public ResponseEntity<Clothe> buscarPrendaPorId(@PathVariable Integer id){
        Optional<Clothe> prenda = clotheService.searchForId(id);
        if(prenda.isPresent()){
            Clothe prendaARetornar = prenda.get();
            return ResponseEntity.ok(prendaARetornar);
        }
        else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    @PutMapping
    public ResponseEntity<String> actualizarPrenda(@RequestBody Clothe clothe){
        Optional<Clothe> prendaOptional = clotheService.searchForId(clothe.getClotheId());
        if (prendaOptional.isPresent()) {
            clotheService.updateClothe(clothe);
            return ResponseEntity.ok("{\"message\": \"prenda modificada\"}");
        } else {
            return new ResponseEntity<>("{\"message\": \"prenda no encontrada\"}", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> borrarPrenda(@PathVariable Integer id){
        Optional<Clothe> prendaOptional = clotheService.searchForId(id);
        if (prendaOptional.isPresent()) {
            clotheService.deleteClothe(id);
            return ResponseEntity.ok("{\"message\": \"prenda eliminada\"}");
        } else {
            return new ResponseEntity<>("{\"message\": \"prenda no encontrada\"}", HttpStatus.NOT_FOUND);
        }
    }
}
