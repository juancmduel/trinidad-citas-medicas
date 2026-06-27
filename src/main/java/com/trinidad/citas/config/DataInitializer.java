package com.trinidad.citas.config;

import com.trinidad.citas.model.Rol;
import com.trinidad.citas.model.Usuario;
import com.trinidad.citas.repository.RolRepository;
import com.trinidad.citas.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final RolRepository rolRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        String passwordHash = passwordEncoder.encode("Trinidad2026");
        log.info("Password hash generated for seed users");

        List<Rol> roles = rolRepository.findAll();
        if (roles.isEmpty()) {
            roles = List.of(
                crearRol("ADMINISTRADOR", "Administrador del sistema"),
                crearRol("GERENTE", "Gerencia / dashboards"),
                crearRol("MEDICO", "Medico especialista"),
                crearRol("RECEPCIONISTA", "Personal de admision"),
                crearRol("ENFERMERA", "Personal de enfermeria / triaje"),
                crearRol("PACIENTE", "Paciente del centro medico")
            );
            roles = rolRepository.saveAll(roles);
            log.info("Created {} roles", roles.size());
        }

        if (usuarioRepository.count() > 0) {
            log.info("Users exist, updating passwords to ensure they match...");
            for (String username : List.of("admin", "gerente", "recepcion", "enfermera", "dr.garcia", "dra.lopez", "paciente1")) {
                usuarioRepository.findByUsername(username).ifPresent(u -> {
                    u.setPasswordHash(passwordHash);
                    u.setActivo(1);
                    u.setBloqueado(0);
                    u.setIntentosFallidos(0);
                    usuarioRepository.save(u);
                    log.info("Updated password for user '{}'", username);
                });
            }
            log.info("All user passwords updated to: Trinidad2026");
            return;
        }

        log.info("No users found, seeding initial data...");

        List<Usuario> usuarios = List.of(
            crearUsuario("admin",     passwordHash, "admin@trinidadtarapoto.com",     "ADMINISTRADOR", roles),
            crearUsuario("gerente",   passwordHash, "gerente@trinidadtarapoto.com",   "GERENTE", roles),
            crearUsuario("recepcion", passwordHash, "recepcion@trinidadtarapoto.com", "RECEPCIONISTA", roles),
            crearUsuario("enfermera", passwordHash, "enfermera@trinidadtarapoto.com", "ENFERMERA", roles),
            crearUsuario("dr.garcia", passwordHash, "dr.garcia@trinidadtarapoto.com", "MEDICO", roles),
            crearUsuario("dra.lopez", passwordHash, "dra.lopez@trinidadtarapoto.com", "MEDICO", roles),
            crearUsuario("paciente1", passwordHash, "paciente1@example.com",          "PACIENTE", roles)
        );
        usuarioRepository.saveAll(usuarios);
        log.info("Created {} users with password: Trinidad2026", usuarios.size());
    }

    private Rol crearRol(String nombre, String descripcion) {
        return Rol.builder().nombre(nombre).descripcion(descripcion).activo(1).build();
    }

    private Usuario crearUsuario(String username, String passwordHash, String email, String rolNombre, List<Rol> roles) {
        Rol rol = roles.stream()
            .filter(r -> r.getNombre().equals(rolNombre))
            .findFirst().orElseThrow(() -> new RuntimeException("Rol not found: " + rolNombre));
        return Usuario.builder()
            .username(username)
            .passwordHash(passwordHash)
            .email(email)
            .activo(1)
            .bloqueado(0)
            .intentosFallidos(0)
            .fechaCreacion(LocalDateTime.now())
            .roles(Set.of(rol))
            .build();
    }
}
