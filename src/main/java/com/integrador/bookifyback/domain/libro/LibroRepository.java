package com.integrador.bookifyback.domain.libro;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface LibroRepository extends JpaRepository<Libro, Long>, JpaSpecificationExecutor<Libro> {

    // Busca libros con la misma categoria o autor, excluyendo el libro actual
    @Query("SELECT l FROM Libro l WHERE (l.categoria.id = :categoriaId OR l.autor.id = :autorId) AND l.id != :libroId ORDER BY l.fechaRegistro DESC")
    List<Libro> findRecomendados(@Param("categoriaId") Long categoriaId, @Param("autorId") Long autorId, @Param("libroId") Long libroId, Pageable pageable);
}