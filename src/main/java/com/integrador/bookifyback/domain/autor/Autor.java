package com.integrador.bookifyback.domain.autor;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "autor")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Autor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String nombre;
}