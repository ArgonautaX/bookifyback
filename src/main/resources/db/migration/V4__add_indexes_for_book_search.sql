-- Indices para busqueda en tabla libro
CREATE INDEX idx_libro_titulo ON libro (titulo);
CREATE INDEX idx_libro_precio ON libro (precio);
CREATE INDEX idx_libro_formato ON libro (formato);
CREATE INDEX idx_libro_estado ON libro (estado);

-- Indice compuesto para filtros combinados frecuentes (estado + precio)
CREATE INDEX idx_libro_estado_precio ON libro (estado, precio);

-- Indices en llaves foraneas
CREATE INDEX idx_libro_autor_id ON libro (autor_id);
CREATE INDEX idx_libro_categoria_id ON libro (categoria_id);

-- Indices para busqueda en tabla autor y categoria
CREATE INDEX idx_autor_nombre ON autor (nombre);
CREATE INDEX idx_categoria_nombre ON categoria (nombre);
