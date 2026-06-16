-- Índice para optimizar el ordenamiento por fecha de registro en Nuevos Lanzamientos
CREATE INDEX idx_libro_fecha_registro ON libro (fecha_registro DESC);
