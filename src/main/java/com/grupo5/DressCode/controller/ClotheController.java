package com.grupo5.DressCode.controller;

import com.grupo5.DressCode.dto.ClotheDTO;
import com.grupo5.DressCode.service.IClotheService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/clothe")
public class ClotheController {

    private final IClotheService clotheService;

    public ClotheController(IClotheService clotheService) {
        this.clotheService = clotheService;
    }

    @PostMapping
    public ResponseEntity<ClotheDTO> crearPrenda(@RequestBody ClotheDTO clotheDTO) {
        ClotheDTO prendaCreada = clotheService.createClothe(clotheDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(prendaCreada);
    }

    @GetMapping
    public ResponseEntity<List<ClotheDTO>> traerTodos() {
        List<ClotheDTO> prendasDTO = clotheService.searchAll();
        return ResponseEntity.ok(prendasDTO);
    }

    @GetMapping("/delete")
    public ResponseEntity<List<ClotheDTO>> traerTodosEliminada() {
        List<ClotheDTO> prendasDTO = clotheService.searchAllDelete();
        return ResponseEntity.ok(prendasDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClotheDTO> buscarPrendaPorId(@PathVariable Integer id) {
        Optional<ClotheDTO> prendaDTO = clotheService.searchForId(id);
        return prendaDTO.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> actualizarClothe(@PathVariable Integer id, @RequestBody ClotheDTO clotheDTO) {
        try {
            System.out.println("ID recibido: " + id);
            clotheService.updateClothe(id, clotheDTO);
            return ResponseEntity.ok("{\"message\": \"Prenda modificada\"}");
        } catch (RuntimeException e) {
            return new ResponseEntity<>("{\"message\": \"" + e.getMessage() + "\"}", HttpStatus.NOT_FOUND);
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> borrarPrenda(@PathVariable Integer id) {
        Optional<ClotheDTO> prendaOptional = clotheService.searchForId(id);
        if (prendaOptional.isPresent()) {
            clotheService.deleteClothe(id);
            return ResponseEntity.ok("{\"message\": \"Prenda eliminada correctamente\"}");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\": \"Prenda no encontrada\"}");
        }
    }

    /*@GetMapping("/search")
    public ResponseEntity<List<Object[]>> searchClothes(@RequestParam String name) {
        name = name.trim();  // Elimina espacios y saltos de línea antes de buscar
        System.out.println("Buscando ropa con nombre: '" + name + "'");  // Verifica que name esté limpio
        List<Object[]> clothes = clotheService.searchName(name);
        return new ResponseEntity<>(clothes, HttpStatus.OK);
    }*/
    @GetMapping("/search")
    public ResponseEntity<List<ClotheDTO>> searchClothes(@RequestParam String name) {
        name = name.trim();
        List<ClotheDTO> clothes = clotheService.searchName(name);
        return new ResponseEntity<>(clothes, HttpStatus.OK);
    }

}
