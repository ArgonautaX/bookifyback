package com.integrador.bookifyback.domain.libro;

import com.integrador.bookifyback.domain.autor.Autor;
import com.integrador.bookifyback.domain.categoria.Categoria;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.*;

@Entity
@Table(name = "libro")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String titulo;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String descripcion;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @Column(nullable = false, length = 20)
    private String formato;

    @Column(name = "portada_url")
    private String portadaUrl;

    @Column(name = "archivo_url")
    private String archivoUrl;

    @Column(nullable = false)
    private Boolean estado;

    @Column(name = "fecha_registro", nullable = false)
    private LocalDateTime fechaRegistro;

    @ManyToOne
    @JoinColumn(name = "autor_id", nullable = false)
    private Autor autor;

    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    @PrePersist
    void prePersist() {
        if (estado == null) {
            estado = true;
        }

        if (fechaRegistro == null) {
            fechaRegistro = LocalDateTime.now();
        }
    }
}