package com.integrador.bookifyback.domain.usuario.exception;

public class CredencialesInvalidasException extends RuntimeException {

    public CredencialesInvalidasException() {
        super("Credenciales invalidas");
    }
}
