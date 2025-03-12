package com.grupo5.DressCode.controller;


import com.grupo5.DressCode.dto.ClothesDTO;
import com.grupo5.DressCode.entity.Clothe;
import com.grupo5.DressCode.service.IClotheService;
import com.grupo5.DressCode.service.impl.ClotheService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/clothe")
public class ClotheController {

    private final IClotheService clotheService;
    private final ClotheService clotheServiceImpl;

    public ClotheController(IClotheService clotheService, ClotheService clotheServiceImpl) {
        this.clotheService = clotheService;
        this.clotheServiceImpl = clotheServiceImpl;
    }

    // ✅ Crear una nueva prenda
    @PostMapping
    public ResponseEntity<ClothesDTO> crearPrenda(@RequestBody ClothesDTO clothesDTO) {
        Clothe prendaCreada = clotheService.createClothe(clothesDTO);
        if (prendaCreada != null) {
            // Convertimos la entidad a DTO para devolverla correctamente formateada
            ClothesDTO prendaDTO = clotheServiceImpl.convertToDTO(prendaCreada);
            return ResponseEntity.status(HttpStatus.CREATED).body(prendaDTO);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // ✅ Obtener todas las prendas (usando DTO)
    @GetMapping
    public ResponseEntity<List<ClothesDTO>> traerTodos() {
        List<ClothesDTO> prendasDTO = clotheServiceImpl.listAllClothes();
        return ResponseEntity.ok(prendasDTO);
    }

    // ✅ Buscar prenda por ID (usando DTO)
    @GetMapping("/{id}")
    public ResponseEntity<ClothesDTO> buscarPrendaPorId(@PathVariable Integer id) {
        try {
            ClothesDTO prendaDTO = clotheServiceImpl.getClotheById(id);
            return ResponseEntity.ok(prendaDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // ✅ Actualizar prenda (devuelve DTO)
    @PutMapping("/{id}")
    public ResponseEntity<ClothesDTO> actualizarPrenda(@PathVariable int id, @RequestBody ClothesDTO clothesDTO) {
        try {
            Clothe updatedClothe = clotheService.updateClothe(id, clothesDTO);
            ClothesDTO updatedClotheDTO = clotheServiceImpl.convertToDTO(updatedClothe);
            return ResponseEntity.ok(updatedClotheDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // ✅ Eliminar prenda
    @DeleteMapping("/{id}")
    public ResponseEntity<String> borrarPrenda(@PathVariable Integer id) {
        Optional<Clothe> prendaOptional = clotheService.searchForId(id);
        if (prendaOptional.isPresent()) {
            clotheService.deleteClothe(id);
            return ResponseEntity.ok("{\"message\": \"Prenda eliminada correctamente\"}");
        } else {
            return new ResponseEntity<>("{\"message\": \"Prenda no encontrada\"}", HttpStatus.NOT_FOUND);
        }
    }
}
