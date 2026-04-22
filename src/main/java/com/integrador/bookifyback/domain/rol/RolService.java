package com.integrador.bookifyback.domain.rol;

import com.integrador.bookifyback.domain.rol.exception.RolNoEncontradoException;
import org.springframework.stereotype.Service;

@Service
public class RolService {

    private static final String ROL_CLIENTE = "ROLE_CLIENTE";

    private final RolRepository rolRepository;

    public RolService(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    public Rol obtenerRolCliente() {
        return rolRepository.findByNombre(ROL_CLIENTE)
                .orElseThrow(() -> new RolNoEncontradoException(ROL_CLIENTE));
    }
}
