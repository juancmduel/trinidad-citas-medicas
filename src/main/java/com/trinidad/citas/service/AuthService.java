package com.trinidad.citas.service;

import com.trinidad.citas.config.JwtService;
import com.trinidad.citas.dto.LoginRequest;
import com.trinidad.citas.dto.LoginResponse;
import com.trinidad.citas.model.Usuario;
import com.trinidad.citas.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;
    private final UsuarioRepository usuarioRepository;

    public LoginResponse login(LoginRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        String token = jwtService.generateToken(userDetails);

        Usuario usuario = usuarioRepository.findByUsername(request.getUsername()).orElseThrow();
        usuario.setFechaUltLogin(LocalDateTime.now());
        usuarioRepository.save(usuario);

        return LoginResponse.builder()
            .token(token)
            .tipo("Bearer")
            .username(usuario.getUsername())
            .email(usuario.getEmail())
            .roles(usuario.getRoles().stream().map(r -> r.getNombre()).collect(Collectors.toSet()))
            .expiracionMs(jwtService.getExpirationMs())
            .build();
    }
}
