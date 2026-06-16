package com.integrador.bookifyback.domain.categoria;

import com.integrador.bookifyback.domain.categoria.dto.CategoriaRequest;
import com.integrador.bookifyback.domain.categoria.dto.CategoriaResponse;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @org.springframework.cache.annotation.Cacheable("categorias")
    public List<CategoriaResponse> listarTodos() {
        return categoriaRepository.findAll().stream()
                .map(CategoriaResponse::from)
                .toList();
    }

    @org.springframework.cache.annotation.CacheEvict(value = "categorias", allEntries = true)
    public CategoriaResponse crear(CategoriaRequest request) {
        Categoria categoria = new Categoria();
        categoria.setNombre(request.nombre().trim());
        return CategoriaResponse.from(categoriaRepository.save(categoria));
    }
}