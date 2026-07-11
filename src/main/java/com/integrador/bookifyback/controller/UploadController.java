package com.integrador.bookifyback.controller;

import com.integrador.bookifyback.domain.imagen.CloudinaryService;
import com.integrador.bookifyback.domain.libro.LibroService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Slf4j

@RestController
@RequestMapping("/upload")
public class UploadController {

    private final CloudinaryService cloudinaryService;
    private final LibroService libroService;

    public UploadController(CloudinaryService cloudinaryService, LibroService libroService) {
        this.cloudinaryService = cloudinaryService;
        this.libroService = libroService;
    }

    @PostMapping("/imagen")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> subirImagen(@RequestParam("archivo") MultipartFile archivo) {
        if (archivo.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "El archivo está vacío"));
        }

        String contentType = archivo.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            return ResponseEntity.badRequest().body(Map.of("error", "Solo se permiten imágenes"));
        }

        String url = cloudinaryService.subirImagen(archivo);
        return ResponseEntity.ok(Map.of("url", url));
    }

    @PostMapping("/libro/{libroId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> subirArchivoLibro(@PathVariable Long libroId, @RequestParam("archivo") MultipartFile archivo, Authentication authentication) {
        log.info("=== UPLOAD DEBUG ===");
        log.info("Authentication: {}", authentication);
        log.info("Authenticated: {}", authentication != null && authentication.isAuthenticated());
        log.info("Principal: {}", authentication != null ? authentication.getName() : null);
        log.info("Authorities: {}", authentication != null ? authentication.getAuthorities() : null);

        if (archivo.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "El archivo está vacío"));
        }

        String url = cloudinaryService.subirArchivo(archivo);
        libroService.actualizarArchivoUrl(libroId, url);
        return ResponseEntity.ok(Map.of("url", url));
    }

    @DeleteMapping("/imagen")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> eliminarImagenCancelada(@RequestBody Map<String, String> body) {
        String url = body.get("url");
        if (url == null || url.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "La URL es obligatoria"));
        }

        cloudinaryService.eliminarImagen(url);
        return ResponseEntity.ok(Map.of("mensaje", "Imagen eliminada correctamente"));
    }
}
