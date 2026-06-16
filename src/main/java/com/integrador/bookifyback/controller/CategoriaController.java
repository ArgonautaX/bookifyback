package com.integrador.bookifyback.controller;

import java.util.List;
import com.integrador.bookifyback.domain.categoria.Categoria;
import com.integrador.bookifyback.domain.categoria.CategoriaService; // <-- Importamos el Service
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/categorias")
public class CategoriaController {

    private final CategoriaService categoriaService; // <-- Usamos el Service

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @GetMapping
    public ResponseEntity<List<Categoria>> listar() {
        return ResponseEntity.ok(categoriaService.listarTodos());
    }

    @PostMapping
    @org.springframework.security.access.prepost.PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Categoria> crear(@RequestBody Categoria categoria) {
        return ResponseEntity.status(org.springframework.http.HttpStatus.CREATED).body(categoriaService.crear(categoria));
    }
}