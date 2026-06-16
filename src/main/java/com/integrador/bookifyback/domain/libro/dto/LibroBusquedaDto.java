package com.integrador.bookifyback.domain.libro.dto;

import com.integrador.bookifyback.domain.libro.Libro;
import java.math.BigDecimal;

public record LibroBusquedaDto(
        Long id,
        String titulo,
        String descripcion,
        BigDecimal precio,
        String formato,
        String portadaUrl,
        String autorNombre,
        String categoriaNombre
) {
    public static LibroBusquedaDto from(Libro libro) {
        return new LibroBusquedaDto(
                libro.getId(),
                libro.getTitulo(),
                libro.getDescripcion(),
                libro.getPrecio(),
                libro.getFormato(),
                libro.getPortadaUrl(),
                libro.getAutor().getNombre(),
                libro.getCategoria().getNombre()
        );
    }
}
