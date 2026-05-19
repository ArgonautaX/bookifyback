package com.integrador.bookifyback.domain.usuario;

import com.integrador.bookifyback.domain.rol.Rol;
import com.integrador.bookifyback.domain.rol.RolService;
import com.integrador.bookifyback.domain.usuario.dto.LoginRequest;
import com.integrador.bookifyback.domain.usuario.dto.LoginResponse;
import com.integrador.bookifyback.domain.usuario.dto.RegisterRequest;
import com.integrador.bookifyback.domain.usuario.dto.RegisterResponse;
import com.integrador.bookifyback.domain.usuario.exception.CredencialesInvalidasException;
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

    public LoginResponse login(LoginRequest request) {
        String correoNormalizado = request.getCorreo().trim().toLowerCase();

        Usuario usuario = usuarioRepository.findByCorreo(correoNormalizado)
                .orElseThrow(CredencialesInvalidasException::new);

        if (!passwordEncoder.matches(request.getContrasena(), usuario.getClave())) {
            throw new CredencialesInvalidasException();
        }

        return LoginResponse.builder()
                .exito(true)
                .mensaje("Login exitoso")
                .usuarioId(usuario.getId())
                .correo(usuario.getCorreo())
                .build();
    }
}
