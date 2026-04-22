package com.integrador.bookifyback.domain.usuario;

import com.integrador.bookifyback.domain.rol.Rol;
import com.integrador.bookifyback.domain.rol.RolService;
import com.integrador.bookifyback.domain.usuario.dto.RegisterRequest;
import com.integrador.bookifyback.domain.usuario.dto.RegisterResponse;
import com.integrador.bookifyback.domain.usuario.exception.CorreoDuplicadoException;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolService rolService;
    private final BCryptPasswordEncoder passwordEncoder;

    public UsuarioService(
            UsuarioRepository usuarioRepository,
            RolService rolService,
            BCryptPasswordEncoder passwordEncoder
    ) {
        this.usuarioRepository = usuarioRepository;
        this.rolService = rolService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public RegisterResponse registrar(RegisterRequest request) {
        String correoNormalizado = request.getCorreo().trim().toLowerCase();

        if (usuarioRepository.existsByCorreo(correoNormalizado)) {
            throw new CorreoDuplicadoException(correoNormalizado);
        }

        Rol rolCliente = rolService.obtenerRolCliente();

        Usuario usuario = Usuario.builder()
                .nombre(request.getNombre().trim())
                .correo(correoNormalizado)
                .clave(passwordEncoder.encode(request.getContrasena()))
                .build();

        usuario.getRoles().add(rolCliente);

        Usuario guardado = usuarioRepository.save(usuario);

        return RegisterResponse.builder()
                .exito(true)
                .mensaje("Usuario registrado correctamente")
                .usuarioId(guardado.getId())
                .correo(guardado.getCorreo())
                .build();
    }
}
