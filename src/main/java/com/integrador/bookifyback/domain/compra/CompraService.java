package com.integrador.bookifyback.domain.compra;

import com.integrador.bookifyback.domain.libro.Libro;
import com.integrador.bookifyback.domain.libro.LibroRepository;
import com.integrador.bookifyback.domain.usuario.Usuario;
import com.integrador.bookifyback.domain.usuario.UsuarioRepository;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.resources.preference.Preference;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.resources.payment.Payment;
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

                        // 3. Guardar en Base de Datos como PENDIENTE primero para tener el ID
                        Compra compra = Compra.builder()
                                        .usuario(usuario)
                                        .libro(libro)
                                        .monto(libro.getPrecio())
                                        .estado("PENDIENTE")
                                        .build();
                        compra = compraRepository.save(compra);

                        // 4. Crear Preferencia de Mercado Pago con URLs de retorno
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
                                        .externalReference(compra.getId().toString())
                                        .notificationUrl("https://bookify-webhook.loca.lt/api/compras/webhook")
                                        .build();

                        PreferenceClient client = new PreferenceClient();
                        Preference preference = client.create(preferenceRequest);
                        String preferenceId = preference.getId();

                        // Actualizar la compra con el preferenceId
                        compra.setMpPreferenceId(preferenceId);
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

        @Transactional
        public void procesarWebhook(java.util.Map<String, Object> payload) {
                try {
                        String type = (String) payload.get("type");
                        if ("payment".equals(type)) {
                                java.util.Map<String, Object> data = (java.util.Map<String, Object>) payload.get("data");
                                if (data != null && data.get("id") != null) {
                                        String paymentIdStr = data.get("id").toString();
                                        Long paymentId = Long.parseLong(paymentIdStr);

                                        PaymentClient client = new PaymentClient();
                                        Payment payment = client.get(paymentId);

                                        String externalReference = payment.getExternalReference();
                                        String status = payment.getStatus();

                                        if (externalReference != null) {
                                                Long compraId = Long.parseLong(externalReference);
                                                compraRepository.findById(compraId).ifPresent(compra -> {
                                                        if ("approved".equals(status)) {
                                                                compra.setEstado("COMPLETADA");
                                                        } else if ("rejected".equals(status)) {
                                                                compra.setEstado("FALLIDA");
                                                        }
                                                        compraRepository.save(compra);
                                                        log.info("Webhook recibido. Compra actualizada: {} - Estado: {}", compra.getId(), compra.getEstado());
                                                });
                                        }
                                }
                        }
                } catch (Exception e) {
                        log.error("Error procesando el webhook de Mercado Pago", e);
                }
        }
}
