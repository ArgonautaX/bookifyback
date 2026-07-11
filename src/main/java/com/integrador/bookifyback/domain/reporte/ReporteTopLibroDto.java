package com.integrador.bookifyback.domain.reporte;

import java.math.BigDecimal;

public record ReporteTopLibroDto(
    String titulo,
    String autorNombre,
    String categoriaNombre,
    Long cantidadVendida,
    BigDecimal ingresos
) {}
