package com.integrador.bookifyback.domain.categoria.exception;

public class CategoriaNoEncontradaException extends RuntimeException {

    public CategoriaNoEncontradaException(Long id) {
        super("No se encontro la categoria con id: " + id);
    }
}