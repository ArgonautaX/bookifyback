package com.integrador.bookifyback.controller;

import com.integrador.bookifyback.domain.libro.Libro;
import com.integrador.bookifyback.domain.libro.LibroService;
import com.integrador.bookifyback.domain.libro.dto.FiltroLibroDto;
import com.integrador.bookifyback.domain.libro.dto.LibroBusquedaDto;
import com.integrador.bookifyback.domain.libro.dto.LibroRequest;
import com.integrador.bookifyback.domain.libro.dto.LibroResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/libros")
public class LibroController {

    private final LibroService libroService;

    public LibroController(LibroService libroService) {
        this.libroService = libroService;
    }

    @GetMapping("/buscar")
    public ResponseEntity<Page<LibroBusquedaDto>> buscar(
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false) Long autorId,
            @RequestParam(required = false) Long categoriaId,
            @RequestParam(required = false) BigDecimal precioMin,
            @RequestParam(required = false) BigDecimal precioMax,
            @RequestParam(required = false) String formato,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(defaultValue = "titulo") String sort
    ) {
        FiltroLibroDto filtro = new FiltroLibroDto(titulo, autorId, categoriaId, precioMin, precioMax, formato);
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort).ascending());
        return ResponseEntity.ok(libroService.buscar(filtro, pageable));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
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