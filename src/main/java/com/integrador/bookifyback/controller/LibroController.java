package com.integrador.bookifyback.controller;

import com.integrador.bookifyback.domain.libro.Libro;
import com.integrador.bookifyback.domain.libro.LibroService;
import com.integrador.bookifyback.domain.libro.dto.FiltroLibroDto;
import com.integrador.bookifyback.domain.libro.dto.LibroBusquedaDto;
import com.integrador.bookifyback.domain.libro.dto.LibroRequest;
import com.integrador.bookifyback.domain.libro.dto.LibroResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

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
            @RequestParam(required = false) String autorNombre,
            @RequestParam(required = false) Long categoriaId,
            @RequestParam(required = false) String categoriaNombre,
            @RequestParam(required = false) BigDecimal precioMin,
            @RequestParam(required = false) BigDecimal precioMax,
            @RequestParam(required = false) String formato,
            @RequestParam(required = false) Boolean estado,
            @org.springframework.data.web.PageableDefault(size = 12, sort = "titulo", direction = org.springframework.data.domain.Sort.Direction.ASC) Pageable pageable) {
        FiltroLibroDto filtro = new FiltroLibroDto(titulo, autorId, autorNombre, categoriaId, categoriaNombre,
                precioMin, precioMax, formato, estado);
        return ResponseEntity.ok(libroService.buscar(filtro, pageable));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LibroResponse> registrar(
            @Valid @RequestBody LibroRequest request) {
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

    @GetMapping("/top")
    public ResponseEntity<List<com.integrador.bookifyback.domain.libro.dto.LibroBusquedaDto>> obtenerMasVendidos(
            @RequestParam(defaultValue = "siempre") String periodo) {
        return ResponseEntity.ok(libroService.obtenerMasVendidos(periodo));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Libro> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(libroService.obtenerPorId(id));
    }

    @GetMapping("/{id}/similares")
    public ResponseEntity<List<com.integrador.bookifyback.domain.libro.dto.LibroBusquedaDto>> obtenerSimilares(@PathVariable Long id) {
        return ResponseEntity.ok(libroService.obtenerSimilares(id));
    }

    @PatchMapping("/{id}/portada")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LibroResponse> actualizarPortada(
            @PathVariable Long id,
            @RequestBody java.util.Map<String, String> body) {
        String nuevaUrl = body.get("portadaUrl");
        if (nuevaUrl == null || nuevaUrl.isBlank()) {
            return ResponseEntity.badRequest().body(
                    LibroResponse.builder().exito(false).mensaje("La URL de portada es obligatoria").build());
        }
        return ResponseEntity.ok(libroService.actualizarPortada(id, nuevaUrl));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LibroResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody LibroRequest request) {
        return ResponseEntity.ok(libroService.actualizar(id, request));
    }

    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LibroResponse> cambiarEstado(
            @PathVariable Long id,
            @RequestBody java.util.Map<String, Boolean> body) {
        Boolean activo = body.get("activo");
        if (activo == null) {
            return ResponseEntity.badRequest().body(
                    LibroResponse.builder().exito(false).mensaje("El campo 'activo' es obligatorio").build());
        }
        return ResponseEntity.ok(libroService.cambiarEstado(id, activo));
    }
}