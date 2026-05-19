package com.integrador.bookifyback.domain.autor.exception;

public class AutorNoEncontradoException extends RuntimeException {

    public AutorNoEncontradoException(Long id) {
        super("No se encontro el autor con id: " + id);
    }
}