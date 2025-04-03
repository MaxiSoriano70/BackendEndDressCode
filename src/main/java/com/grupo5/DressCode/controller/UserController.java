package com.grupo5.DressCode.controller;

import com.grupo5.DressCode.dto.UserDTO;
import com.grupo5.DressCode.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private IUserService userService;

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
        UserDTO createdUserDTO = userService.createUser(userDTO);
        if (createdUserDTO == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else {
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUserDTO);
        }
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.searchAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Integer id) {
        Optional<UserDTO> userDTO = userService.searchForId(id);
        if (userDTO.isPresent()) {
            return ResponseEntity.ok(userDTO.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Integer id, @RequestBody UserDTO userDTO) {
        Optional<UserDTO> updatedUserDTO = userService.updateUser(id, userDTO);
        if (updatedUserDTO.isPresent()) {
            return ResponseEntity.ok("{\"message\": \"user actualizado\"}");
        } else {
            return new ResponseEntity<>("{\"message\": \"user error al actualizar\"}", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Integer id) {
        Optional<UserDTO> userDTO = userService.searchForId(id);
        if (userDTO.isPresent()) {
            userService.deleteUser(id);
            return ResponseEntity.ok("{\"message\": \"user eliminado\"}");
        } else {
            return new ResponseEntity<>("{\"message\": \"user error al eliminar\"}", HttpStatus.NOT_FOUND);
        }
    }

    // Agregar favorito
    @PostMapping("/{userId}/favorite/{clotheId}")
    public ResponseEntity<UserDTO> addFavorite(@PathVariable Integer userId, @PathVariable Integer clotheId) {
        Optional<UserDTO> updatedUserDTO = userService.addFavorite(userId, clotheId);
        if (updatedUserDTO.isPresent()) {
            return ResponseEntity.ok(updatedUserDTO.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // Eliminar favorito
    @DeleteMapping("/{userId}/favorite/{clotheId}")
    public ResponseEntity<UserDTO> removeFavorite(@PathVariable Integer userId, @PathVariable Integer clotheId) {
        Optional<UserDTO> updatedUserDTO = userService.removeFavorite(userId, clotheId);
        if (updatedUserDTO.isPresent()) {
            return ResponseEntity.ok(updatedUserDTO.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}