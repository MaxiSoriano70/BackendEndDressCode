package com.grupo5.DressCode.controller;

import com.grupo5.DressCode.entity.Attribute;
import com.grupo5.DressCode.security.entity.User;
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

    public AttributeController(IAttributeService attributeService){
        this.attributeService = attributeService;
    }

    @PostMapping
    public ResponseEntity<Attribute> crearAtributo(@RequestBody Attribute attribute){
        Attribute atributoCreado = attributeService.createAttribute(attribute);
        if(atributoCreado == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }else{
            return ResponseEntity.status(HttpStatus.CREATED).body(atributoCreado);
        }
    }

    @GetMapping
    public ResponseEntity<List<Attribute>> traerTodos() {
        return ResponseEntity.ok(attributeService.searchAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Attribute> buscarAtributoPorId(@PathVariable Integer id) {
        Optional<Attribute> atributo = attributeService.searchForId(id);
        return atributo.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> actualizarAtributo(@PathVariable Integer id, @RequestBody Attribute attribute){
        Optional<Attribute> attributeOptional = attributeService.updateAttribute(id, attribute);
        if (attributeOptional.isPresent()) {
            return ResponseEntity.ok("{\"message\": \"atributo modificado\"}");
        } else {
            return new ResponseEntity<>("{\"message\": \"atributo no encontrado\"}", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> borrarAtributo(@PathVariable Integer id) {
        Optional<Attribute> atributoOptional = attributeService.searchForId(id);
        if (atributoOptional.isPresent()) {
            attributeService.deleteAttribute(id);
            return ResponseEntity.ok("{\"message\": \"atributo eliminado\"}");
        } else {
            return new ResponseEntity<>("{\"message\": \"atributo no encontrado\"}", HttpStatus.NOT_FOUND);
        }
    }
}
