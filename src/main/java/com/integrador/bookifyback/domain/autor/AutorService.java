package com.integrador.bookifyback.domain.autor;

import com.integrador.bookifyback.domain.autor.dto.AutorRequest;
import com.integrador.bookifyback.domain.autor.dto.AutorResponse;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AutorService {

    private final AutorRepository autorRepository;

    public AutorService(AutorRepository autorRepository) {
        this.autorRepository = autorRepository;
    }

    @org.springframework.cache.annotation.Cacheable("autores")
    public List<AutorResponse> listarTodos() {
        return autorRepository.findAll().stream()
                .map(AutorResponse::from)
                .toList();
    }

    @org.springframework.cache.annotation.CacheEvict(value = "autores", allEntries = true)
    public AutorResponse crear(AutorRequest request) {
        Autor autor = new Autor();
        autor.setNombre(request.nombre().trim());
        return AutorResponse.from(autorRepository.save(autor));
    }
}