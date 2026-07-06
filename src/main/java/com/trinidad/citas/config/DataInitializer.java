package com.trinidad.citas.config;

import com.trinidad.citas.model.Rol;
import com.trinidad.citas.model.Usuario;
import com.trinidad.citas.repository.RolRepository;
import com.trinidad.citas.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * Cada vez que arranca la aplicación, esto se ejecuta para asegurarse
 * de que los roles y usuarios básicos existan.
 *
 * Si no hay roles, los crea (ADMINISTRADOR, MEDICO, etc.).
 * Si no hay usuarios, crea los de prueba (admin, gerente, dr.garcia, etc.).
 * Si ya hay usuarios, solo actualiza sus contraseñas por si cambió la variable
 * de entorno TRINIDAD_SEED_USER_PASSWORD.
 *
 * La contraseña de los usuarios semilla se toma de una variable de entorno,
 * NUNCA está hardcodeada en el código. En desarrollo se usa "Trinidad2026".
 */
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final RolRepository rolRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Contraseña para los usuarios semilla (admin, gerente, etc.).
     * Se lee desde variable de entorno TRINIDAD_SEED_USER_PASSWORD.
     * En desarrollo se usa el valor por defecto 'Trinidad2026'.
     * Jamás hardcodear la contraseña real en el código fuente.
     */
    @Value("${trinidad.seed.user-password:Trinidad2026}")
    private String seedPassword;

    /**
     * Al arrancar:
     *  1. Genera el hash BCrypt de la contraseña semilla
     *  2. Limpia la variable en memoria (por seguridad)
     *  3. Crea roles si no existen
     *  4. Crea o actualiza usuarios semilla
     */
    @Override
    @Transactional
    public void run(String... args) {
        String passwordHash = passwordEncoder.encode(seedPassword);
        log.info("🔐 Hash BCrypt generado para usuarios semilla");

        // Limpiamos la contraseña en texto plano de la memoria apenas la usamos
        seedPassword = null;

        // ── Roles ─────────────────────────────────────────────────
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
            log.info("✅ Creados {} roles", roles.size());
        }

        // ── Usuarios ──────────────────────────────────────────────
        if (usuarioRepository.count() > 0) {
            // Si ya hay usuarios, solo actualizamos sus contraseñas
            // (por si el admin cambió la variable de entorno)
            log.info("🔄 Usuarios existentes, sincronizando contraseñas...");
            for (String username : List.of("admin", "gerente", "recepcion", "enfermera", "dr.garcia", "dra.lopez", "paciente1")) {
                usuarioRepository.findByUsername(username).ifPresent(u -> {
                    u.setPasswordHash(passwordHash);
                    u.setActivo(1);
                    u.setBloqueado(0);
                    u.setIntentosFallidos(0);
                    usuarioRepository.save(u);
                    log.info("  ✓ Contraseña actualizada para '{}'", username);
                });
            }
            log.info("✅ Contraseñas sincronizadas desde variable de entorno");
            return;
        }

        // Primera ejecución: creamos todos los usuarios semilla
        log.info("🆕 No hay usuarios, creando datos iniciales...");

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
        log.info("✅ Creados {} usuarios con contraseña desde variable de entorno", usuarios.size());
    }

    /** Helper para crear un rol rápido */
    private Rol crearRol(String nombre, String descripcion) {
        return Rol.builder().nombre(nombre).descripcion(descripcion).activo(1).build();
    }

    /** Helper para crear un usuario con su rol asignado */
    private Usuario crearUsuario(String username, String passwordHash, String email, String rolNombre, List<Rol> roles) {
        Rol rol = roles.stream()
            .filter(r -> r.getNombre().equals(rolNombre))
            .findFirst().orElseThrow(() -> new RuntimeException("Rol no encontrado: " + rolNombre));
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
