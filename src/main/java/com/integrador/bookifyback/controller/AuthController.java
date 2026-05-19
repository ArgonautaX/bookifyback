package com.integrador.bookifyback.controller;

import com.integrador.bookifyback.domain.usuario.UsuarioService;
import com.integrador.bookifyback.domain.usuario.dto.LoginRequest;
import com.integrador.bookifyback.domain.usuario.dto.LoginResponse;
import com.integrador.bookifyback.domain.usuario.dto.RegisterRequest;
import com.integrador.bookifyback.domain.usuario.dto.RegisterResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UsuarioService usuarioService;

    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest request) {
        RegisterResponse response = usuarioService.registrar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = usuarioService.login(request);
        return ResponseEntity.ok(response);
    }
}
