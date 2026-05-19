CREATE TABLE autor (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(120) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE categoria (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE libro (
    id BIGINT NOT NULL AUTO_INCREMENT,
    titulo VARCHAR(150) NOT NULL,
    descripcion TEXT NOT NULL,
    precio DECIMAL(10, 2) NOT NULL,
    formato VARCHAR(20) NOT NULL,
    portada_url VARCHAR(255),
    estado BIT NOT NULL,
    fecha_registro DATETIME NOT NULL,
    autor_id BIGINT NOT NULL,
    categoria_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_libro_autor FOREIGN KEY (autor_id) REFERENCES autor (id),
    CONSTRAINT fk_libro_categoria FOREIGN KEY (categoria_id) REFERENCES categoria (id)
);
