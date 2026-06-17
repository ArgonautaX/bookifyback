package com.integrador.bookifyback.controller;

import com.integrador.bookifyback.domain.compra.CompraRequest;
import com.integrador.bookifyback.domain.compra.CompraResponse;
import com.integrador.bookifyback.domain.compra.CompraService;
import com.integrador.bookifyback.domain.compra.SimularPagoRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/compras")
@RequiredArgsConstructor
public class CompraController {

    private final CompraService compraService;

    @PostMapping("/iniciar")
    public ResponseEntity<CompraResponse> iniciarCompra(@RequestBody CompraRequest request,
            Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String correo = userDetails.getUsername(); // El correo del cliente autenticado

        String preferenceId = compraService.iniciarCompra(request.libroId(), correo);

        return ResponseEntity.ok(new CompraResponse(preferenceId));
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> recibirWebhook(@RequestBody java.util.Map<String, Object> payload) {
        compraService.procesarWebhook(payload);
        return ResponseEntity.ok("OK");
    }

    @PostMapping("/simular-pago")
    public ResponseEntity<String> simularPago(@RequestBody SimularPagoRequest request) {
        compraService.simularPagoDirecto(request.preferenceId());
        return ResponseEntity.ok("Pago procesado correctamente");
    }

    @GetMapping("/mis-libros")
    public ResponseEntity<java.util.List<com.integrador.bookifyback.domain.libro.Libro>> obtenerMisLibros(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return ResponseEntity.ok(compraService.obtenerMisLibros(userDetails.getUsername()));
    }

    @GetMapping("/verificar-acceso/{libroId}")
    public ResponseEntity<?> verificarAcceso(@PathVariable Long libroId, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        boolean tieneAcceso = compraService.verificarAcceso(libroId, userDetails.getUsername());
        
        if (tieneAcceso) {
            return ResponseEntity.ok(java.util.Map.of("acceso", true));
        } else {
            return ResponseEntity.status(org.springframework.http.HttpStatus.FORBIDDEN)
                    .body(java.util.Map.of("acceso", false, "mensaje", "No tienes acceso a este libro"));
        }
    }
}
