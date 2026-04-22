package com.integrador.bookifyback.domain.usuario.exception;

public class CorreoDuplicadoException extends RuntimeException {

    public CorreoDuplicadoException(String correo) {
        super("El correo ya se encuentra registrado: " + correo);
    }
}
