package com.integrador.bookifyback.controller;

import java.util.List;
import com.integrador.bookifyback.domain.autor.Autor;
import com.integrador.bookifyback.domain.autor.AutorService; // <-- Importamos el Service
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/autores")
public class AutorController {

    private final AutorService autorService; // <-- Usamos el Service

    public AutorController(AutorService autorService) {
        this.autorService = autorService;
    }

    @GetMapping
    public ResponseEntity<List<Autor>> listar() {
        return ResponseEntity.ok(autorService.listarTodos());
    }

    @PostMapping
    @org.springframework.security.access.prepost.PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Autor> crear(@RequestBody Autor autor) {
        return ResponseEntity.status(org.springframework.http.HttpStatus.CREATED).body(autorService.crear(autor));
    }
}