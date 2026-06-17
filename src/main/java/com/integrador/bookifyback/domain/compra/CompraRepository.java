package com.integrador.bookifyback.domain.compra;

import com.integrador.bookifyback.domain.libro.Libro;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface CompraRepository extends JpaRepository<Compra, Long> {

    // Obtener los libros más populares de todos los tiempos
    @Query("SELECT c.libro FROM Compra c WHERE c.estado = 'COMPLETADA' GROUP BY c.libro ORDER BY COUNT(c.id) DESC")
    List<Libro> findMasPopulares(Pageable pageable);

    // Obtener los libros más vendidos a partir de una fecha (ej: inicio del día,
    // inicio de semana)
    @Query("SELECT c.libro FROM Compra c WHERE c.estado = 'COMPLETADA' AND c.fechaCompra >= :desde GROUP BY c.libro ORDER BY COUNT(c.id) DESC")
    List<Libro> findMasVendidosDesde(@Param("desde") LocalDateTime desde, Pageable pageable);

    java.util.Optional<Compra> findByMpPreferenceId(String mpPreferenceId);
}
