CREATE TABLE compra (
    id BIGINT NOT NULL AUTO_INCREMENT,
    usuario_id BIGINT NOT NULL,
    libro_id BIGINT NOT NULL,
    monto DECIMAL(10, 2) NOT NULL,
    fecha_compra DATETIME NOT NULL,
    estado VARCHAR(30) NOT NULL,
    mp_preference_id VARCHAR(255),
    mp_payment_id VARCHAR(255),
    PRIMARY KEY (id),
    CONSTRAINT fk_compra_usuario FOREIGN KEY (usuario_id) REFERENCES usuario (id),
    CONSTRAINT fk_compra_libro FOREIGN KEY (libro_id) REFERENCES libro (id)
);

CREATE INDEX idx_compra_fecha ON compra (fecha_compra);
CREATE INDEX idx_compra_estado ON compra (estado);
CREATE INDEX idx_compra_libro ON compra (libro_id);
