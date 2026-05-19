package com.integrador.bookifyback.domain.libro.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LibroResponse {

    private boolean exito;
    private String mensaje;
    private Long libroId;
}