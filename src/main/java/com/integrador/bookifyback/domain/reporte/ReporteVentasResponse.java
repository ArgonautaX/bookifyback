package com.integrador.bookifyback.domain.reporte;

import java.util.List;

public record ReporteVentasResponse(
    ReporteVentasResumenDto resumen,
    List<ReporteTopLibroDto> topLibros,
    List<ReporteVentasCategoriaDto> ventasPorCategoria,
    List<ReporteVentasAutorDto> ventasPorAutor
) {}
