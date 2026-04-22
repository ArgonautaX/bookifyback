package com.integrador.bookifyback.domain.rol.exception;

public class RolNoEncontradoException extends RuntimeException {

    public RolNoEncontradoException(String nombreRol) {
        super("No se encontro el rol requerido: " + nombreRol);
    }
}
