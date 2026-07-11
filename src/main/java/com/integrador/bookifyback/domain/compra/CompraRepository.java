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
    @Query("SELECT c.libro FROM Compra c WHERE c.estado = 'COMPLETADA' AND c.libro.estado = true GROUP BY c.libro ORDER BY COUNT(c.id) DESC")
    List<Libro> findMasPopulares(Pageable pageable);

    // Obtener los libros más vendidos a partir de una fecha (ej: inicio del día,
    // inicio de semana)
    @Query("SELECT c.libro FROM Compra c WHERE c.estado = 'COMPLETADA' AND c.libro.estado = true AND c.fechaCompra >= :desde GROUP BY c.libro ORDER BY COUNT(c.id) DESC")
    List<Libro> findMasVendidosDesde(@Param("desde") LocalDateTime desde, Pageable pageable);

    @Query("SELECT DISTINCT l FROM Compra c JOIN c.libro l JOIN FETCH l.autor JOIN FETCH l.categoria WHERE c.usuario.correo = :correo AND c.estado = :estado")
    List<Libro> findLibrosByUsuarioCorreoAndEstado(@Param("correo") String correo, @Param("estado") String estado);

    @Query("SELECT c FROM Compra c JOIN FETCH c.libro l JOIN FETCH l.autor WHERE c.usuario.correo = :correo " +
           "AND (cast(:fechaInicio as timestamp) IS NULL OR c.fechaCompra >= :fechaInicio) " +
           "AND (cast(:fechaFin as timestamp) IS NULL OR c.fechaCompra <= :fechaFin) " +
           "ORDER BY c.fechaCompra DESC")
    List<Compra> findHistorialByUsuarioCorreoAndFechas(
            @Param("correo") String correo,
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin
    );

    boolean existsByUsuarioCorreoAndLibroIdAndEstado(String correo, Long libroId, String estado);

    @Query("SELECT COALESCE(SUM(c.monto), 0) FROM Compra c WHERE c.estado = 'COMPLETADA' AND c.fechaCompra BETWEEN :inicio AND :fin")
    java.math.BigDecimal sumIngresosByFechaBetween(@Param("inicio") LocalDateTime inicio, @Param("fin") LocalDateTime fin);

    @Query("SELECT COUNT(c) FROM Compra c WHERE c.estado = 'COMPLETADA' AND c.fechaCompra BETWEEN :inicio AND :fin")
    Long countCompletadasByFechaBetween(@Param("inicio") LocalDateTime inicio, @Param("fin") LocalDateTime fin);

    @Query("SELECT c.libro.titulo, c.libro.autor.nombre, c.libro.categoria.nombre, COUNT(c), SUM(c.monto) " +
           "FROM Compra c WHERE c.estado = 'COMPLETADA' AND c.fechaCompra BETWEEN :inicio AND :fin " +
           "GROUP BY c.libro.id, c.libro.titulo, c.libro.autor.nombre, c.libro.categoria.nombre " +
           "ORDER BY COUNT(c) DESC")
    List<Object[]> findTopLibrosVendidos(@Param("inicio") LocalDateTime inicio, @Param("fin") LocalDateTime fin, Pageable pageable);

    @Query("SELECT c.libro.categoria.nombre, COUNT(c), SUM(c.monto) " +
           "FROM Compra c WHERE c.estado = 'COMPLETADA' AND c.fechaCompra BETWEEN :inicio AND :fin " +
           "GROUP BY c.libro.categoria.id, c.libro.categoria.nombre " +
           "ORDER BY COUNT(c) DESC")
    List<Object[]> findVentasByCategoria(@Param("inicio") LocalDateTime inicio, @Param("fin") LocalDateTime fin);

    @Query("SELECT c.libro.autor.nombre, COUNT(c), SUM(c.monto) " +
           "FROM Compra c WHERE c.estado = 'COMPLETADA' AND c.fechaCompra BETWEEN :inicio AND :fin " +
           "GROUP BY c.libro.autor.id, c.libro.autor.nombre " +
           "ORDER BY COUNT(c) DESC")
    List<Object[]> findVentasByAutor(@Param("inicio") LocalDateTime inicio, @Param("fin") LocalDateTime fin);
}
