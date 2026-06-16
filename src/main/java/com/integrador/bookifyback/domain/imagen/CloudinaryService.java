package com.integrador.bookifyback.domain.imagen;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public String subirImagen(MultipartFile archivo) {
        try {
            Map<?, ?> resultado = cloudinary.uploader().upload(
                    archivo.getBytes(),
                    ObjectUtils.asMap(
                            "folder", "bookify/portadas",
                            "resource_type", "image"
                    )
            );
            return (String) resultado.get("secure_url");
        } catch (IOException e) {
            throw new RuntimeException("Error al subir la imagen a Cloudinary", e);
        }
    }

    public void eliminarImagen(String url) {
        if (url == null || url.isBlank()) return;
        try {
            String publicId = extraerPublicId(url);
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        } catch (IOException e) {
            throw new RuntimeException("Error al eliminar la imagen de Cloudinary", e);
        }
    }

    private String extraerPublicId(String url) {
        int inicio = url.indexOf("/bookify/portadas/");
        if (inicio == -1) return "";
        String segmento = url.substring(inicio + 1);
        int punto = segmento.lastIndexOf(".");
        return punto != -1 ? segmento.substring(0, punto) : segmento;
    }
}
