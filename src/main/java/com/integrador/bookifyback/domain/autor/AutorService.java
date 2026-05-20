package com.integrador.bookifyback.domain.autor;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AutorService {

    private final AutorRepository autorRepository;

    public AutorService(AutorRepository autorRepository) {
        this.autorRepository = autorRepository;
    }

    public List<Autor> listarTodos() {
        return autorRepository.findAll();
    }
}