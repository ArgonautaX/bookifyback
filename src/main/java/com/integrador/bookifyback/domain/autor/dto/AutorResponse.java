package com.integrador.bookifyback.domain.autor.dto;

import com.integrador.bookifyback.domain.autor.Autor;

public record AutorResponse(
        Long id,
        String nombre
) {
    public static AutorResponse from(Autor autor) {
        return new AutorResponse(autor.getId(), autor.getNombre());
    }
}
