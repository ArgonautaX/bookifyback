package com.integrador.bookifyback.domain.usuario.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {

    private boolean exito;
    private String mensaje;
    private Long usuarioId;
    private String correo;
}
