package com.integrador.bookifyback.domain.libro;

import com.integrador.bookifyback.domain.autor.Autor;
import com.integrador.bookifyback.domain.autor.AutorRepository;
import com.integrador.bookifyback.domain.autor.exception.AutorNoEncontradoException;
import com.integrador.bookifyback.domain.categoria.Categoria;
import com.integrador.bookifyback.domain.categoria.CategoriaRepository;
import com.integrador.bookifyback.domain.categoria.exception.CategoriaNoEncontradaException;
import com.integrador.bookifyback.domain.libro.dto.LibroRequest;
import com.integrador.bookifyback.domain.libro.dto.LibroResponse;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class LibroService {

    private final LibroRepository libroRepository;
    private final AutorRepository autorRepository;
    private final CategoriaRepository categoriaRepository;

    public LibroService(
            LibroRepository libroRepository,
            AutorRepository autorRepository,
            CategoriaRepository categoriaRepository
    ) {
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
        this.categoriaRepository = categoriaRepository;
    }
    public List<Libro> listarTodos() {
        return libroRepository.findAll();
    }
    @Transactional
    public LibroResponse registrar(LibroRequest request) {

        Autor autor = autorRepository.findById(request.getAutorId())
                .orElseThrow(() ->
                        new AutorNoEncontradoException(request.getAutorId()));

        Categoria categoria = categoriaRepository.findById(request.getCategoriaId())
                .orElseThrow(() ->
                        new CategoriaNoEncontradaException(request.getCategoriaId()));

        Libro libro = Libro.builder()
                .titulo(request.getTitulo().trim())
                .descripcion(request.getDescripcion().trim())
                .precio(request.getPrecio())
                .formato(request.getFormato().trim())
                .portadaUrl(request.getPortadaUrl())
                .autor(autor)
                .categoria(categoria)
                .build();

        Libro guardado = libroRepository.save(libro);

        return LibroResponse.builder()
                .exito(true)
                .mensaje("Libro registrado correctamente")
                .libroId(guardado.getId())
                .build();
    }
}