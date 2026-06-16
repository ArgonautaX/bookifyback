package com.integrador.bookifyback.domain.libro;

import com.integrador.bookifyback.domain.libro.dto.FiltroLibroDto;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class LibroSpecification {

    private LibroSpecification() {}

    public static Specification<Libro> conFiltros(FiltroLibroDto filtro) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (Long.class != query.getResultType() && long.class != query.getResultType()) {
                root.fetch("autor", JoinType.LEFT);
                root.fetch("categoria", JoinType.LEFT);
            }
            query.distinct(true);

            predicates.add(cb.isTrue(root.get("estado")));

            if (StringUtils.hasText(filtro.titulo())) {
                predicates.add(cb.like(
                        cb.lower(root.get("titulo")),
                        "%" + filtro.titulo().toLowerCase() + "%"
                ));
            }

            if (filtro.autorId() != null) {
                predicates.add(cb.equal(root.get("autor").get("id"), filtro.autorId()));
            }

            if (StringUtils.hasText(filtro.autorNombre())) {
                predicates.add(cb.like(
                        cb.lower(root.get("autor").get("nombre")),
                        "%" + filtro.autorNombre().toLowerCase() + "%"
                ));
            }

            if (filtro.categoriaId() != null) {
                predicates.add(cb.equal(root.get("categoria").get("id"), filtro.categoriaId()));
            }

            if (StringUtils.hasText(filtro.categoriaNombre())) {
                predicates.add(cb.like(
                        cb.lower(root.get("categoria").get("nombre")),
                        "%" + filtro.categoriaNombre().toLowerCase() + "%"
                ));
            }

            if (filtro.precioMin() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("precio"), filtro.precioMin()));
            }

            if (filtro.precioMax() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("precio"), filtro.precioMax()));
            }

            if (StringUtils.hasText(filtro.formato())) {
                predicates.add(cb.equal(
                        cb.lower(root.get("formato")),
                        filtro.formato().toLowerCase()
                ));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
