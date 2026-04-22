CREATE TABLE rol (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(50) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_rol_nombre UNIQUE (nombre)
);

CREATE TABLE usuario (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(120) NOT NULL,
    correo VARCHAR(150) NOT NULL,
    clave VARCHAR(255) NOT NULL,
    fecha_registro DATETIME NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_usuario_correo UNIQUE (correo)
);

CREATE TABLE usuario_rol (
    usuario_id BIGINT NOT NULL,
    rol_id BIGINT NOT NULL,
    PRIMARY KEY (usuario_id, rol_id),
    CONSTRAINT fk_usuario_rol_usuario FOREIGN KEY (usuario_id) REFERENCES usuario (id),
    CONSTRAINT fk_usuario_rol_rol FOREIGN KEY (rol_id) REFERENCES rol (id)
);
