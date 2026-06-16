package com.integrador.bookifyback.domain.libro.dto;

import java.math.BigDecimal;

public record FiltroLibroDto(
                String titulo,
                Long autorId,
                String autorNombre,
                Long categoriaId,
                String categoriaNombre,
                BigDecimal precioMin,
                BigDecimal precioMax,
                String formato) {
}
