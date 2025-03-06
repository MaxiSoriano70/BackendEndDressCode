package com.grupo5.DressCode.controller;

import com.grupo5.DressCode.dto.UserDTO;
import com.grupo5.DressCode.security.entity.User;
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
    public ResponseEntity<User> createUser(@RequestBody User user){
        User userToReturn = userService.createUser(user);
        if(user == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }else {
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        }
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers(){
        return ResponseEntity.ok(userService.searchAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Integer id){
        Optional<UserDTO> userDTO = userService.searchForId(id);
        if(userDTO.isPresent()){
            return ResponseEntity.ok(userDTO.get());
        }
        else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Integer id, @RequestBody User user){
        Optional<User> userOptional = userService.updateUser(id, user);
        if (userOptional.isPresent()) {
            return ResponseEntity.ok("{\"message\": \"user updated\"}");
        } else {
            return new ResponseEntity<>("{\"message\": \"user not found\"}", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Integer id){
        Optional<UserDTO> userOptional = userService.searchForId(id);
        if (userOptional.isPresent()) {
            userService.deleteUser(id);
            return ResponseEntity.ok("{\"message\": \"user deleted\"}");
        } else {
            return new ResponseEntity<>("{\"message\": \"user not found\"}", HttpStatus.NOT_FOUND);
        }
    }
}
