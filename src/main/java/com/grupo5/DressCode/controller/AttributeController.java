package com.grupo5.DressCode.controller;

import com.grupo5.DressCode.dto.AttributeDTO;
import com.grupo5.DressCode.service.IAttributeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/attribute")
public class AttributeController {
    private final IAttributeService attributeService;

    public AttributeController(IAttributeService attributeService) {
        this.attributeService = attributeService;
    }

    @PostMapping
    public ResponseEntity<AttributeDTO> crearAtributo(@RequestBody AttributeDTO attributeDTO) {
        AttributeDTO atributoCreado = attributeService.createAttribute(attributeDTO);
        if (atributoCreado == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else {
            return ResponseEntity.status(HttpStatus.CREATED).body(atributoCreado);
        }
    }

    @GetMapping
    public ResponseEntity<List<AttributeDTO>> traerTodos() {
        List<AttributeDTO> attributes = attributeService.searchAll();
        return ResponseEntity.ok(attributes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AttributeDTO> buscarAtributoPorId(@PathVariable Integer id) {
        Optional<AttributeDTO> atributo = attributeService.searchForId(id);
        return atributo.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> actualizarAtributo(@PathVariable Integer id, @RequestBody AttributeDTO attributeDTO) {
        Optional<AttributeDTO> attributeOptional = attributeService.updateAttribute(id, attributeDTO);
        if (attributeOptional.isPresent()) {
            return ResponseEntity.ok("{\"message\": \"atributo modificado\"}");
        } else {
            return new ResponseEntity<>("{\"message\": \"atributo no encontrado\"}", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> borrarAtributo(@PathVariable Integer id) {
        Optional<AttributeDTO> atributoOptional = attributeService.searchForId(id);
        if (atributoOptional.isPresent()) {
            attributeService.deleteAttribute(id);
            return ResponseEntity.ok("{\"message\": \"atributo eliminado\"}");
        } else {
            return new ResponseEntity<>("{\"message\": \"atributo no encontrado\"}", HttpStatus.NOT_FOUND);
        }
    }
}
