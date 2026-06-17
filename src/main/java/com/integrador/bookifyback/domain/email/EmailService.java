package com.integrador.bookifyback.domain.email;

import com.integrador.bookifyback.domain.compra.Compra;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;

    @Async
    public void enviarReciboCompra(Long compraId, String estado, String destinatario, String nombreUsuario, String tituloLibro, String autorLibro, BigDecimal monto) {
        String asunto = "";
        String contenidoHtml = "";

        // Preparamos los mensajes según el estado
        if ("COMPLETADA".equals(estado)) {
            asunto = "¡Compra Exitosa en Bookify! - " + tituloLibro;
            contenidoHtml = generarHtmlComprobante(compraId, nombreUsuario, tituloLibro, autorLibro, monto);
        } else if ("RECHAZADA".equals(estado) || "FALLIDA".equals(estado)) {
            asunto = "Problemas con tu pago en Bookify - " + tituloLibro;
            contenidoHtml = generarHtmlRechazado(nombreUsuario, tituloLibro);
        } else {
            // No enviamos correos para estados PENDIENTES o CANCELADAS por ahora.
            return;
        }

        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(destinatario);
            helper.setSubject(asunto);
            helper.setText(contenidoHtml, true);

            javaMailSender.send(message);
            log.info("Correo electrónico enviado exitosamente a {} por la compra {}", destinatario, compraId);
        } catch (MessagingException e) {
            log.error("Error al enviar el correo electrónico a {} por la compra {}", destinatario, compraId, e);
        }
    }

    private String generarHtmlComprobante(Long compraId, String nombre, String titulo, String autor, BigDecimal monto) {

        return "<!DOCTYPE html>" +
                "<html lang='es'>" +
                "<head>" +
                "<meta charset='UTF-8'>" +
                "<style>" +
                "body { font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif; background-color: #f4f7f6; color: #333; margin: 0; padding: 0; }" +
                ".container { max-width: 600px; margin: 40px auto; background-color: #ffffff; border-radius: 8px; overflow: hidden; box-shadow: 0 4px 10px rgba(0,0,0,0.1); }" +
                ".header { background-color: #4F46E5; color: #ffffff; padding: 30px 20px; text-align: center; }" +
                ".header h1 { margin: 0; font-size: 24px; letter-spacing: 1px; }" +
                ".content { padding: 30px; }" +
                ".content h2 { color: #4F46E5; font-size: 20px; margin-top: 0; }" +
                ".content p { font-size: 16px; line-height: 1.5; margin-bottom: 20px; }" +
                ".details-box { background-color: #f9fafb; border: 1px solid #e5e7eb; border-radius: 6px; padding: 20px; margin-bottom: 20px; }" +
                ".details-row { display: flex; justify-content: space-between; margin-bottom: 10px; border-bottom: 1px solid #e5e7eb; padding-bottom: 10px; }" +
                ".details-row:last-child { border-bottom: none; margin-bottom: 0; padding-bottom: 0; }" +
                ".details-label { font-weight: bold; color: #6b7280; }" +
                ".details-value { font-weight: 600; color: #111827; text-align: right; }" +
                ".footer { background-color: #f3f4f6; padding: 20px; text-align: center; font-size: 14px; color: #6b7280; border-top: 1px solid #e5e7eb; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='container'>" +
                "<div class='header'>" +
                "<h1>Comprobante de Compra</h1>" +
                "</div>" +
                "<div class='content'>" +
                "<h2>¡Hola, " + nombre + "!</h2>" +
                "<p>Queremos agradecerte por tu compra en Bookify. Tu pago ha sido procesado exitosamente y tu nuevo libro ya está disponible en tu biblioteca digital.</p>" +
                "<div class='details-box'>" +
                "<div class='details-row'><span class='details-label'>Libro:</span><span class='details-value'>" + titulo + "</span></div>" +
                "<div class='details-row'><span class='details-label'>Autor:</span><span class='details-value'>" + autor + "</span></div>" +
                "<div class='details-row'><span class='details-label'>ID de Transacción:</span><span class='details-value'>#" + compraId + "</span></div>" +
                "<div class='details-row'><span class='details-label'>Monto Pagado:</span><span class='details-value' style='color: #10b981; font-size: 18px;'>S/ " + monto + "</span></div>" +
                "</div>" +
                "<p>Puedes acceder a tu libro en cualquier momento iniciando sesión y yendo a la sección <strong>Mis Libros</strong>.</p>" +
                "</div>" +
                "<div class='footer'>" +
                "<p>Gracias por confiar en Bookify. ¡Feliz lectura!</p>" +
                "<p>&copy; 2026 Bookify. Todos los derechos reservados.</p>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";
    }

    private String generarHtmlRechazado(String nombre, String titulo) {

        return "<!DOCTYPE html>" +
                "<html lang='es'>" +
                "<head>" +
                "<meta charset='UTF-8'>" +
                "<style>" +
                "body { font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif; background-color: #f4f7f6; color: #333; margin: 0; padding: 0; }" +
                ".container { max-width: 600px; margin: 40px auto; background-color: #ffffff; border-radius: 8px; overflow: hidden; box-shadow: 0 4px 10px rgba(0,0,0,0.1); }" +
                ".header { background-color: #ef4444; color: #ffffff; padding: 30px 20px; text-align: center; }" +
                ".header h1 { margin: 0; font-size: 24px; letter-spacing: 1px; }" +
                ".content { padding: 30px; }" +
                ".content h2 { color: #ef4444; font-size: 20px; margin-top: 0; }" +
                ".content p { font-size: 16px; line-height: 1.5; margin-bottom: 20px; }" +
                ".footer { background-color: #f3f4f6; padding: 20px; text-align: center; font-size: 14px; color: #6b7280; border-top: 1px solid #e5e7eb; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='container'>" +
                "<div class='header'>" +
                "<h1>Aviso de Pago Rechazado</h1>" +
                "</div>" +
                "<div class='content'>" +
                "<h2>Hola, " + nombre + "</h2>" +
                "<p>Te informamos que hubo un inconveniente al procesar el pago para el libro <strong>" + titulo + "</strong>.</p>" +
                "<p>Por favor, revisa los datos de tu método de pago y vuelve a intentarlo en nuestra plataforma. Tu libro sigue esperándote.</p>" +
                "</div>" +
                "<div class='footer'>" +
                "<p>Si crees que esto es un error, por favor contáctanos.</p>" +
                "<p>&copy; 2026 Bookify. Todos los derechos reservados.</p>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";
    }
}
