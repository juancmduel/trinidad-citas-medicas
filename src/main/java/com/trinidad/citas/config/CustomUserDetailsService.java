package com.trinidad.citas.config;

import com.trinidad.citas.model.Usuario;
import com.trinidad.citas.repository.UsuarioRepository;
import com.trinidad.citas.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        List<GrantedAuthority> authorities = usuario.getRoles().stream()
            .map(rol -> new SimpleGrantedAuthority("ROLE_" + rol.getNombre()))
            .collect(Collectors.toList());

        return new UserPrincipal(
            usuario.getIdUsuario(),
            usuario.getUsername(),
            usuario.getPasswordHash(),
            usuario.isActivo(),
            !usuario.isBloqueado(), // isBloqueado()=true → cuenta bloqueada, Spring espera "not locked"
            authorities
        );
    }
}
