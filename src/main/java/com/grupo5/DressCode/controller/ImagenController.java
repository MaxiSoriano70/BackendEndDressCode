package com.grupo5.DressCode.controller;

import com.grupo5.DressCode.entity.Image;
import com.grupo5.DressCode.service.IImageService;
//import com.grupo5.DressCode.service.impl.ImageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/imagen")
public class ImagenController {
    public IImageService imageService;

    public ImagenController(IImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping
    public ResponseEntity<Image> crearImagen(@RequestBody Image image){
        Image imagenARetornar = imageService.createImage(image);
        if(imagenARetornar == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }else {
            return ResponseEntity.status(HttpStatus.CREATED).body(imagenARetornar);
        }
    }
    @GetMapping
    public ResponseEntity<List<Image>> traerTodos(){
        return ResponseEntity.ok(imageService.searchAll());
    }
    @GetMapping("/{id}")
    public ResponseEntity<Image> buscarImagenPorId(@PathVariable Integer id){
        Optional<Image> imagen = imageService.searchForId(id);
        if(imagen.isPresent()){
            Image imageARetornar = imagen.get();
            return ResponseEntity.ok(imageARetornar);
        }
        else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    @PutMapping
    public ResponseEntity<String> actualizarImagen(@RequestBody Image image){
        Optional<Image> imagenOptional = imageService.searchForId(image.getImagenId());
        if (imagenOptional.isPresent()) {
            imageService.updateImage(image);
            return ResponseEntity.ok("{\"message\": \"imagen modificada\"}");
        } else {
            return new ResponseEntity<>("{\"message\": \"imagen no encontrada\"}", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> borrarImagen(@PathVariable Integer id){
        Optional<Image> imagenOptional = imageService.searchForId(id);
        if (imagenOptional.isPresent()) {
            imageService.deleteImage(id);
            return ResponseEntity.ok("{\"message\": \"imagen eliminada\"}");
        } else {
            return new ResponseEntity<>("{\"message\": \"imagen no encontrada\"}", HttpStatus.NOT_FOUND);
        }
    }
}
