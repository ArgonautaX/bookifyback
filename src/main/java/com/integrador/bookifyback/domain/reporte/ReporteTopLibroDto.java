package com.integrador.bookifyback.domain.reporte;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ReporteTopLibroDto {
    private String titulo;
    private String autorNombre;
    private String categoriaNombre;
    private Long cantidadVendida;
    private BigDecimal ingresos;
}
