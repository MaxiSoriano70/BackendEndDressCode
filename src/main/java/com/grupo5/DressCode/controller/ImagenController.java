package com.grupo5.DressCode.controller;

import com.grupo5.DressCode.dto.ImagenDTO;
import com.grupo5.DressCode.service.IImageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/imagen")
public class ImagenController {
    private final IImageService imageService;

    public ImagenController(IImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping
    public ResponseEntity<ImagenDTO> crearImagen(@RequestBody ImagenDTO imagenDTO) {
        ImagenDTO imagenARetornar = imageService.createImage(imagenDTO);
        if (imagenARetornar == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else {
            return ResponseEntity.status(HttpStatus.CREATED).body(imagenARetornar);
        }
    }

    @GetMapping
    public ResponseEntity<List<ImagenDTO>> traerTodos() {
        return ResponseEntity.ok(imageService.searchAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ImagenDTO> buscarImagenPorId(@PathVariable Integer id) {
        Optional<ImagenDTO> imagenDTO = imageService.searchForId(id);
        return imagenDTO.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> actualizarImagen(@PathVariable Integer id, @RequestBody ImagenDTO imagenDTO) {
        try {
            imageService.updateImage(id, imagenDTO);
            return ResponseEntity.ok("{\"message\": \"Imagen modificada\"}");
        } catch (RuntimeException e) {
            return new ResponseEntity<>("{\"message\": \"" + e.getMessage() + "\"}", HttpStatus.NOT_FOUND);
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> borrarImagen(@PathVariable Integer id) {
        Optional<ImagenDTO> imagenOptional = imageService.searchForId(id);
        if (imagenOptional.isPresent()) {
            imageService.deleteImage(id);
            return ResponseEntity.ok("{\"message\": \"imagen eliminada\"}");
        } else {
            return new ResponseEntity<>("{\"message\": \"imagen no encontrada\"}", HttpStatus.NOT_FOUND);
        }
    }
}
