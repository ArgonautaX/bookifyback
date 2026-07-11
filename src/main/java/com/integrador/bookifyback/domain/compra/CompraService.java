package com.integrador.bookifyback.domain.compra;

import com.integrador.bookifyback.domain.libro.Libro;
import com.integrador.bookifyback.domain.libro.LibroRepository;
import com.integrador.bookifyback.domain.usuario.Usuario;
import com.integrador.bookifyback.domain.usuario.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompraService {

        private final CompraRepository compraRepository;
        private final LibroRepository libroRepository;
        private final UsuarioRepository usuarioRepository;
        private final com.integrador.bookifyback.domain.email.EmailService emailService;

        @Transactional
        public Long iniciarCompra(Long libroId, String correoUsuario) {
                Libro libro = libroRepository.findById(libroId)
                                .orElseThrow(() -> new RuntimeException("Libro no encontrado"));

                Usuario usuario = usuarioRepository.findByCorreo(correoUsuario)
                                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

                if (compraRepository.existsByUsuarioCorreoAndLibroIdAndEstado(correoUsuario, libroId, "COMPLETADA")) {
                        throw new IllegalStateException("El usuario ya ha comprado este libro.");
                }

                Compra compra = Compra.builder()
                                .usuario(usuario)
                                .libro(libro)
                                .monto(libro.getPrecio())
                                .estado("PENDIENTE")
                                .build();
                compra = compraRepository.save(compra);

                log.info("Compra iniciada. ID: {}", compra.getId());
                return compra.getId();
        }

        @Transactional
        public void simularPagoDirecto(Long compraId) {
                compraRepository.findById(compraId).ifPresent(compra -> {
                        compra.setEstado("COMPLETADA");
                        compraRepository.save(compra);
                        log.info("Pago procesado exitosamente: Compra {} marcada como COMPLETADA", compra.getId());

                        emailService.enviarReciboCompra(
                            compra.getId(), 
                            compra.getEstado(), 
                            compra.getUsuario().getCorreo(), 
                            compra.getUsuario().getNombre(), 
                            compra.getLibro().getTitulo(), 
                            compra.getLibro().getAutor().getNombre(), 
                            compra.getMonto()
                        );
                });
        }

        @Transactional(readOnly = true)
        public List<Libro> obtenerMisLibros(String correoUsuario) {
                return compraRepository.findLibrosByUsuarioCorreoAndEstado(correoUsuario, "COMPLETADA");
        }

        @Transactional(readOnly = true)
        public boolean verificarAcceso(Long libroId, String correoUsuario) {
                return compraRepository.existsByUsuarioCorreoAndLibroIdAndEstado(correoUsuario, libroId, "COMPLETADA");
        }

        @Transactional(readOnly = true)
        public List<HistorialCompraDto> obtenerHistorialUsuario(String correoUsuario, java.time.LocalDateTime fechaInicio, java.time.LocalDateTime fechaFin) {
                return compraRepository.findHistorialByUsuarioCorreoAndFechas(correoUsuario, fechaInicio, fechaFin)
                                .stream()
                                .map(HistorialCompraDto::from)
                                .toList();
        }
}
