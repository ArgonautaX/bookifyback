package com.integrador.bookifyback.domain.libro.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LibroRequest {

    @NotBlank(message = "El titulo es obligatorio")
    private String titulo;

    @NotBlank(message = "La descripcion es obligatoria")
    private String descripcion;

    @NotNull(message = "El precio es obligatorio")
    @Positive(message = "El precio debe ser mayor a 0")
    private BigDecimal precio;

    @NotBlank(message = "El formato es obligatorio")
    private String formato;

    private String portadaUrl;

    @NotNull(message = "El autor es obligatorio")
    private Long autorId;

    @NotNull(message = "La categoria es obligatoria")
    private Long categoriaId;
}