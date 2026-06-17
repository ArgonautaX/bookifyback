package com.integrador.bookifyback.config;

import com.mercadopago.MercadoPagoConfig;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class MercadoPagoConfigApp {

    @Value("${mercadopago.access-token}")
    private String accessToken;

    @PostConstruct
    public void init() {
        if (accessToken != null && !accessToken.isBlank()) {
            MercadoPagoConfig.setAccessToken(accessToken);
            log.info("SDK de Mercado Pago inicializado correctamente.");
        } else {
            log.warn("El Access Token de Mercado Pago no está configurado.");
        }
    }
}
