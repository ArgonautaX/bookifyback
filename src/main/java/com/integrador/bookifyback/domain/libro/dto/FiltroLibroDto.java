package com.integrador.bookifyback.domain.libro.dto;

import java.math.BigDecimal;

public record FiltroLibroDto(
        String titulo,
        Long autorId,
        Long categoriaId,
        BigDecimal precioMin,
        BigDecimal precioMax,
        String formato
) {}
