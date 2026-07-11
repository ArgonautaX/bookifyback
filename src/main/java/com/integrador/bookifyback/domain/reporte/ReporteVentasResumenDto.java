package com.integrador.bookifyback.domain.reporte;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ReporteVentasResumenDto(
    Long totalVentas,
    BigDecimal ingresosTotales,
    Long cantidadTransacciones,
    LocalDateTime fechaInicio,
    LocalDateTime fechaFin
) {}
