package com.integrador.bookifyback.domain.compra;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record HistorialCompraDto(
        Long libroId,
        String titulo,
        String portadaUrl,
        String autorNombre,
        BigDecimal monto,
        LocalDateTime fechaCompra,
        String estado
) {
    public static HistorialCompraDto from(Compra compra) {
        return new HistorialCompraDto(
                compra.getLibro().getId(),
                compra.getLibro().getTitulo(),
                compra.getLibro().getPortadaUrl(),
                compra.getLibro().getAutor().getNombre(),
                compra.getMonto(),
                compra.getFechaCompra(),
                compra.getEstado()
        );
    }
}
