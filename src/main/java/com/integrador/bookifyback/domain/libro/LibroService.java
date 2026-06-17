package com.integrador.bookifyback.domain.libro;

import com.integrador.bookifyback.domain.autor.Autor;
import com.integrador.bookifyback.domain.autor.AutorRepository;
import com.integrador.bookifyback.domain.autor.exception.AutorNoEncontradoException;
import com.integrador.bookifyback.domain.categoria.Categoria;
import com.integrador.bookifyback.domain.categoria.CategoriaRepository;
import com.integrador.bookifyback.domain.categoria.exception.CategoriaNoEncontradaException;
import com.integrador.bookifyback.domain.imagen.CloudinaryService;
import com.integrador.bookifyback.domain.libro.dto.FiltroLibroDto;
import com.integrador.bookifyback.domain.libro.dto.LibroBusquedaDto;
import com.integrador.bookifyback.domain.libro.dto.LibroRequest;
import com.integrador.bookifyback.domain.libro.dto.LibroResponse;
import jakarta.transaction.Transactional;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class LibroService {

    private final LibroRepository libroRepository;
    private final AutorRepository autorRepository;
    private final CategoriaRepository categoriaRepository;
    private final CloudinaryService cloudinaryService;
    private final com.integrador.bookifyback.domain.compra.CompraRepository compraRepository;

    public LibroService(
            LibroRepository libroRepository,
            AutorRepository autorRepository,
            CategoriaRepository categoriaRepository,
            CloudinaryService cloudinaryService,
            com.integrador.bookifyback.domain.compra.CompraRepository compraRepository
    ) {
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
        this.categoriaRepository = categoriaRepository;
        this.cloudinaryService = cloudinaryService;
        this.compraRepository = compraRepository;
    }

    public List<Libro> listarTodos() {
        return libroRepository.findAll();
    }

    public Libro obtenerPorId(Long id) {
        return libroRepository.findById(id)
                .orElseThrow(() -> new com.integrador.bookifyback.domain.libro.exception.LibroNoEncontradoException(id));
    }

    public List<LibroBusquedaDto> obtenerSimilares(Long id) {
        Libro libroOriginal = obtenerPorId(id);
        
        // Paginacion para obtener solo los primeros 6 resultados
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(0, 6);
        
        List<Libro> recomendados = libroRepository.findRecomendados(
                libroOriginal.getCategoria().getId(),
                libroOriginal.getAutor().getId(),
                libroOriginal.getId(),
                pageable
        );
        
        return recomendados.stream()
                .map(LibroBusquedaDto::from)
                .toList();
    }

    public List<LibroBusquedaDto> obtenerMasVendidos(String periodo) {
        org.springframework.data.domain.Pageable limit = org.springframework.data.domain.PageRequest.of(0, 8);
        java.time.LocalDateTime desde;
        
        switch (periodo.toLowerCase()) {
            case "hoy":
                desde = java.time.LocalDate.now().atStartOfDay();
                return compraRepository.findMasVendidosDesde(desde, limit).stream().map(LibroBusquedaDto::from).toList();
            case "semana":
                desde = java.time.LocalDate.now().minusDays(7).atStartOfDay();
                return compraRepository.findMasVendidosDesde(desde, limit).stream().map(LibroBusquedaDto::from).toList();
            case "siempre":
            default:
                return compraRepository.findMasPopulares(limit).stream().map(LibroBusquedaDto::from).toList();
        }
    }

    @Cacheable(value = "busquedaLibros", key = "#filtro.toString() + '_' + #pageable.toString()")
    public Page<LibroBusquedaDto> buscar(FiltroLibroDto filtro, Pageable pageable) {
        return libroRepository
                .findAll(LibroSpecification.conFiltros(filtro), pageable)
                .map(LibroBusquedaDto::from);
    }

    @Transactional
    @org.springframework.cache.annotation.CacheEvict(value = "busquedaLibros", allEntries = true)
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

    @Transactional
    @org.springframework.cache.annotation.CacheEvict(value = "busquedaLibros", allEntries = true)
    public LibroResponse actualizarPortada(Long libroId, String nuevaPortadaUrl) {
        Libro libro = libroRepository.findById(libroId)
                .orElseThrow(() -> new RuntimeException("Libro no encontrado con id: " + libroId));

        String portadaAnterior = libro.getPortadaUrl();
        if (portadaAnterior != null && !portadaAnterior.isBlank()) {
            cloudinaryService.eliminarImagen(portadaAnterior);
        }

        libro.setPortadaUrl(nuevaPortadaUrl);
        libroRepository.save(libro);

        return LibroResponse.builder()
                .exito(true)
                .mensaje("Portada actualizada correctamente")
                .libroId(libroId)
                .build();
    }
        @Transactional
        @org.springframework.cache.annotation.CacheEvict(value = "busquedaLibros", allEntries = true)
        public LibroResponse actualizar(Long id, LibroRequest request) {
            Libro libro = obtenerPorId(id);

            Autor autor = autorRepository.findById(request.getAutorId())
                    .orElseThrow(() -> new AutorNoEncontradoException(request.getAutorId()));

            Categoria categoria = categoriaRepository.findById(request.getCategoriaId())
                    .orElseThrow(() -> new CategoriaNoEncontradaException(request.getCategoriaId()));

            libro.setTitulo(request.getTitulo().trim());
            libro.setDescripcion(request.getDescripcion().trim());
            libro.setPrecio(request.getPrecio());
            libro.setFormato(request.getFormato().trim());
            libro.setAutor(autor);
            libro.setCategoria(categoria);
            
            // La portada se maneja en su propio endpoint (actualizarPortada)
            
            libroRepository.save(libro);

            return LibroResponse.builder()
                    .exito(true)
                    .mensaje("Libro actualizado correctamente")
                    .libroId(libro.getId())
                    .build();
        }

        @Transactional
        @org.springframework.cache.annotation.CacheEvict(value = "busquedaLibros", allEntries = true)
        public LibroResponse cambiarEstado(Long id, boolean activo) {
            Libro libro = obtenerPorId(id);
            libro.setEstado(activo);
            libroRepository.save(libro);

            String mensaje = activo ? "Libro restaurado correctamente" : "Libro dado de baja correctamente";
            
            return LibroResponse.builder()
                    .exito(true)
                    .mensaje(mensaje)
                    .libroId(libro.getId())
                    .build();
        }
}