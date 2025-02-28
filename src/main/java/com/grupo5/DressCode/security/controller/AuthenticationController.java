package com.grupo5.DressCode.security.controller;

import com.grupo5.DressCode.security.dto.AuthenticationRequest;
import com.grupo5.DressCode.security.dto.AuthenticationResponse;
import com.grupo5.DressCode.security.dto.RegisterRequest;
import com.grupo5.DressCode.security.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request){
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest request){
        return ResponseEntity.ok(authenticationService.login(request));
    }
}
