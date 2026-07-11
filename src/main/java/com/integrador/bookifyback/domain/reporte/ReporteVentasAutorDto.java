package com.integrador.bookifyback.domain.reporte;

import java.math.BigDecimal;

public record ReporteVentasAutorDto(
    String autorNombre,
    Long cantidadVendida,
    BigDecimal ingresos
) {}
