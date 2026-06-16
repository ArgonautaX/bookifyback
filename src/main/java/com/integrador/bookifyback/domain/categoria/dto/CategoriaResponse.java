package com.integrador.bookifyback.domain.categoria.dto;

import com.integrador.bookifyback.domain.categoria.Categoria;

public record CategoriaResponse(
        Long id,
        String nombre
) {
    public static CategoriaResponse from(Categoria categoria) {
        return new CategoriaResponse(categoria.getId(), categoria.getNombre());
    }
}
