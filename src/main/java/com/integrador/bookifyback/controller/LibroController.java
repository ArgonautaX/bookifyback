package com.integrador.bookifyback.controller;

import com.integrador.bookifyback.domain.libro.Libro;
import com.integrador.bookifyback.domain.libro.LibroService;
import com.integrador.bookifyback.domain.libro.dto.LibroRequest;
import com.integrador.bookifyback.domain.libro.dto.LibroResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@CrossOrigin(origins = "http://localhost:4200") // <-- Agregamos esta línea
@RestController
@RequestMapping("/libros")
public class LibroController {

    private final LibroService libroService;

    public LibroController(LibroService libroService) {
        this.libroService = libroService;
    }

    @PostMapping
    public ResponseEntity<LibroResponse> registrar(
            @Valid @RequestBody LibroRequest request
    ) {

        LibroResponse response = libroService.registrar(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
    @GetMapping
    public ResponseEntity<List<Libro>> listar() {
        List<Libro> libros = libroService.listarTodos();
        return ResponseEntity.ok(libros);
    }
}