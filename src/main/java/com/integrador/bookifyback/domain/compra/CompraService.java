package com.integrador.bookifyback.domain.compra;

import com.integrador.bookifyback.domain.libro.Libro;
import com.integrador.bookifyback.domain.libro.LibroRepository;
import com.integrador.bookifyback.domain.usuario.Usuario;
import com.integrador.bookifyback.domain.usuario.UsuarioRepository;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.resources.preference.Preference;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompraService {

        private final CompraRepository compraRepository;
        private final LibroRepository libroRepository;
        private final UsuarioRepository usuarioRepository;

        @Transactional
        public String iniciarCompra(Long libroId, String correoUsuario) {
                try {
                        // 1. Obtener Entidades
                        Libro libro = libroRepository.findById(libroId)
                                        .orElseThrow(() -> new RuntimeException("Libro no encontrado"));

                        Usuario usuario = usuarioRepository.findByCorreo(correoUsuario)
                                        .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

                        // 2. Crear Item para Mercado Pago
                        PreferenceItemRequest itemRequest = PreferenceItemRequest.builder()
                                        .id(libro.getId().toString())
                                        .title(libro.getTitulo())
                                        .description("Compra de libro digital en Bookify")
                                        .pictureUrl(libro.getPortadaUrl())
                                        .categoryId("books")
                                        .quantity(1)
                                        .currencyId("PEN") // Soles peruanos (ajusta si usas otra moneda)
                                        .unitPrice(libro.getPrecio())
                                        .build();

                        List<PreferenceItemRequest> items = new ArrayList<>();
                        items.add(itemRequest);

                        // 3. Crear Preferencia de Mercado Pago con URLs de retorno
                        com.mercadopago.client.preference.PreferenceBackUrlsRequest backUrls = com.mercadopago.client.preference.PreferenceBackUrlsRequest
                                        .builder()
                                        .success("https://bookify-test.com/pago-exitoso")
                                        .pending("https://bookify-test.com/pago-pendiente")
                                        .failure("https://bookify-test.com/pago-fallido")
                                        .build();

                        PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                                        .items(items)
                                        .backUrls(backUrls)
                                        .autoReturn("approved")
                                        .build();

                        PreferenceClient client = new PreferenceClient();
                        Preference preference = client.create(preferenceRequest);
                        String preferenceId = preference.getId();

                        // 4. Guardar en Base de Datos como PENDIENTE
                        Compra compra = Compra.builder()
                                        .usuario(usuario)
                                        .libro(libro)
                                        .monto(libro.getPrecio())
                                        .estado("PENDIENTE")
                                        .mpPreferenceId(preferenceId)
                                        .build();

                        compraRepository.save(compra);

                        log.info("Preferencia de MercadoPago creada con éxito. ID: {}", preferenceId);
                        return preferenceId;

                } catch (com.mercadopago.exceptions.MPApiException apiException) {
                        log.error("Error devuelto por la API de Mercado Pago. Código de estado: {}", apiException.getApiResponse().getStatusCode());
                        log.error("Detalle del error de Mercado Pago: {}", apiException.getApiResponse().getContent());
                        throw new RuntimeException("La API de Mercado Pago rechazó la petición. Revisa los logs del backend para más detalles.");
                } catch (Exception e) {
                        log.error("Error al iniciar la compra en Mercado Pago", e);
                        throw new RuntimeException("No se pudo iniciar la transacción con Mercado Pago");
                }
        }
}
