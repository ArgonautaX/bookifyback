package com.integrador.bookifyback.domain.reporte;

import java.math.BigDecimal;

public record ReporteVentasCategoriaDto(
    String categoriaNombre,
    Long cantidadVendida,
    BigDecimal ingresos
) {}
