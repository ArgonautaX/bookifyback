package com.integrador.bookifyback.domain.compra;

import com.integrador.bookifyback.domain.libro.Libro;
import com.integrador.bookifyback.domain.usuario.Usuario;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "compra")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Compra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "libro_id", nullable = false)
    private Libro libro;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal monto;

    @Column(name = "fecha_compra", nullable = false)
    private LocalDateTime fechaCompra;

    @Column(nullable = false, length = 30)
    private String estado; // PENDIENTE, COMPLETADA, FALLIDA, CANCELADA

    @Column(name = "mp_preference_id", length = 255)
    private String mpPreferenceId;

    @Column(name = "mp_payment_id", length = 255)
    private String mpPaymentId;

    @PrePersist
    void prePersist() {
        if (fechaCompra == null) {
            fechaCompra = LocalDateTime.now();
        }
        if (estado == null) {
            estado = "PENDIENTE";
        }
    }
}
