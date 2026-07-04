package com.trinidad.citas.config;

import com.trinidad.citas.security.RateLimitingFilter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Value("${trinidad.cors.allowed-origins:}")
    private String allowedOrigins;

    private final JwtAuthFilter jwtAuthFilter;
    private final RateLimitingFilter rateLimitingFilter;
    private final UserDetailsService userDetailsService;
    private final CustomAuthenticationSuccessHandler successHandler;
    private final CustomAuthenticationFailureHandler failureHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        if (allowedOrigins != null && !allowedOrigins.isBlank()) {
            config.setAllowedOrigins(List.of(allowedOrigins.split(",")));
            config.setAllowCredentials(true);
        } else {
            config.setAllowedOriginPatterns(List.of());
        }
        config.setAllowedMethods(List.of(
            HttpMethod.GET.name(), HttpMethod.POST.name(),
            HttpMethod.PUT.name(), HttpMethod.PATCH.name(),
            HttpMethod.DELETE.name(), HttpMethod.OPTIONS.name()
        ));
        config.setAllowedHeaders(List.of("*"));
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", config);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf
                .ignoringRequestMatchers(
                    new AntPathRequestMatcher("/api/**"),
                    new AntPathRequestMatcher("/h2-console/**")
                )
            )
            .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))
            .authorizeHttpRequests(auth -> auth
                // ════════════════════════════════════════════════════════════
                //  RUTAS PÚBLICAS (sin autenticación)
                // ════════════════════════════════════════════════════════════
                .requestMatchers(
                    "/", "/index", "/login", "/registro",
                    "/olvide-password", "/reset-password/**",
                    "/css/**", "/js/**", "/img/**", "/webjars/**",
                    "/h2-console/**", "/error",
                    "/api/auth/**", "/api/v1/auth/**",
                    // Swagger / OpenAPI
                    "/swagger-ui/**", "/swagger-ui.html",
                    "/v3/api-docs/**", "/v3/api-docs.yaml",
                    "/swagger-resources/**"
                ).permitAll()

                // ════════════════════════════════════════════════════════════
                //  API REST — protegido por método HTTP y rol (capa gruesa)
                //  La validación fina está en @PreAuthorize de cada controlador
                // ════════════════════════════════════════════════════════════

                // ── Usuarios y Roles (solo ADMIN) ──
                .requestMatchers(HttpMethod.GET, "/api/v1/usuarios/**", "/api/v1/roles/**").hasRole("ADMINISTRADOR")
                .requestMatchers(HttpMethod.POST, "/api/v1/usuarios/**", "/api/v1/roles/**").hasRole("ADMINISTRADOR")
                .requestMatchers(HttpMethod.PUT, "/api/v1/usuarios/**", "/api/v1/roles/**").hasRole("ADMINISTRADOR")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/usuarios/**", "/api/v1/roles/**").hasRole("ADMINISTRADOR")

                // ── Pacientes ──
                .requestMatchers(HttpMethod.GET, "/api/v1/pacientes/**").hasAnyRole("ADMINISTRADOR", "GERENTE", "RECEPCIONISTA", "ENFERMERA", "MEDICO", "PACIENTE")
                .requestMatchers(HttpMethod.POST, "/api/v1/pacientes/**").hasAnyRole("ADMINISTRADOR", "GERENTE", "RECEPCIONISTA", "ENFERMERA")
                .requestMatchers(HttpMethod.PUT, "/api/v1/pacientes/**").hasAnyRole("ADMINISTRADOR", "GERENTE", "RECEPCIONISTA")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/pacientes/**").hasRole("ADMINISTRADOR")

                // ── Médicos ──
                .requestMatchers(HttpMethod.GET, "/api/v1/medicos/**").hasAnyRole("ADMINISTRADOR", "GERENTE", "RECEPCIONISTA", "ENFERMERA", "MEDICO", "PACIENTE")
                .requestMatchers(HttpMethod.POST, "/api/v1/medicos/**").hasAnyRole("ADMINISTRADOR", "GERENTE")
                .requestMatchers(HttpMethod.PUT, "/api/v1/medicos/**").hasAnyRole("ADMINISTRADOR", "GERENTE")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/medicos/**").hasRole("ADMINISTRADOR")

                // ── Citas ──
                .requestMatchers(HttpMethod.GET, "/api/v1/citas/**").hasAnyRole("ADMINISTRADOR", "GERENTE", "MEDICO", "RECEPCIONISTA", "ENFERMERA", "PACIENTE")
                .requestMatchers(HttpMethod.POST, "/api/v1/citas/**").hasAnyRole("ADMINISTRADOR", "GERENTE", "RECEPCIONISTA", "PACIENTE", "ENFERMERA", "MEDICO")
                .requestMatchers(HttpMethod.PUT, "/api/v1/citas/**").hasAnyRole("ADMINISTRADOR", "GERENTE", "MEDICO")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/citas/**").hasAnyRole("ADMINISTRADOR", "GERENTE", "RECEPCIONISTA")

                // ── Pagos ──
                .requestMatchers(HttpMethod.GET, "/api/v1/pagos/**").hasAnyRole("ADMINISTRADOR", "GERENTE", "RECEPCIONISTA", "ENFERMERA")
                .requestMatchers(HttpMethod.POST, "/api/v1/pagos/**").hasAnyRole("ADMINISTRADOR", "GERENTE", "RECEPCIONISTA")
                .requestMatchers(HttpMethod.PUT, "/api/v1/pagos/**").hasAnyRole("ADMINISTRADOR", "GERENTE")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/pagos/**").hasRole("ADMINISTRADOR")

                // ── Módulos Médicos (Atenciones, Recetas, Órdenes, Diagnósticos, HC) ──
                .requestMatchers("/api/v1/atenciones/**", "/api/v1/recetas/**",
                        "/api/v1/ordenes-examen/**", "/api/v1/historias-clinicas/**",
                        "/api/v1/detalles-receta/**", "/api/v1/diagnosticos/**",
                        "/api/v1/antecedentes/**", "/api/v1/medicacion-actual/**")
                        .hasAnyRole("ADMINISTRADOR", "GERENTE", "MEDICO", "ENFERMERA")

                // ── Triaje ──
                .requestMatchers("/api/v1/triaje/**").hasAnyRole("ADMINISTRADOR", "GERENTE", "ENFERMERA", "MEDICO")

                // ── Horarios ──
                .requestMatchers(HttpMethod.GET, "/api/v1/horarios/**").hasAnyRole("ADMINISTRADOR", "GERENTE", "RECEPCIONISTA", "MEDICO", "ENFERMERA", "PACIENTE")
                .requestMatchers(HttpMethod.POST, "/api/v1/horarios/**").hasAnyRole("ADMINISTRADOR", "GERENTE")
                .requestMatchers(HttpMethod.PUT, "/api/v1/horarios/**").hasAnyRole("ADMINISTRADOR", "GERENTE")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/horarios/**").hasRole("ADMINISTRADOR")

                // ── Especialidades ──
                .requestMatchers(HttpMethod.GET, "/api/v1/especialidades/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/v1/especialidades/**").hasAnyRole("ADMINISTRADOR", "GERENTE")
                .requestMatchers(HttpMethod.PUT, "/api/v1/especialidades/**").hasAnyRole("ADMINISTRADOR", "GERENTE")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/especialidades/**").hasRole("ADMINISTRADOR")

                // ── Dashboard y Reportes ──
                .requestMatchers("/api/v1/dashboard/**", "/api/v1/reportes/**")
                        .hasAnyRole("ADMINISTRADOR", "GERENTE", "MEDICO", "RECEPCIONISTA", "ENFERMERA")

                // ── Configuración y Auditoría ──
                .requestMatchers("/api/v1/configuracion/**", "/api/v1/auditoria/**")
                        .hasAnyRole("ADMINISTRADOR", "GERENTE")

                // ── Cualquier otro endpoint API no cubierto arriba ──
                .requestMatchers("/api/v1/**").authenticated()

                // ════════════════════════════════════════════════════════════
                //  ADMINISTRADOR — control total
                // ════════════════════════════════════════════════════════════
                .requestMatchers(
                    "/usuarios/**", "/roles/**",
                    "/auditoria/**", "/configuracion/**"
                ).hasRole("ADMINISTRADOR")

                // ════════════════════════════════════════════════════════════
                //  REPORTES — ADMIN + GERENTE
                // ════════════════════════════════════════════════════════════
                .requestMatchers("/reportes/**").hasAnyRole("ADMINISTRADOR", "GERENTE")

                // ════════════════════════════════════════════════════════════
                //  DASHBOARD — ADMIN + GERENTE + MEDICO + RECEPCIONISTA
                // ════════════════════════════════════════════════════════════
                .requestMatchers("/dashboard/**").hasAnyRole("ADMINISTRADOR", "GERENTE", "RECEPCIONISTA")

                // ════════════════════════════════════════════════════════════
                //  MÓDULOS BASE — ADMIN + GERENTE + MEDICO + RECEPCIONISTA
                // ════════════════════════════════════════════════════════════
                .requestMatchers("/pacientes/**", "/horarios/**")
                    .hasAnyRole("ADMINISTRADOR", "GERENTE", "RECEPCIONISTA", "ENFERMERA")
                .requestMatchers("/medicos/**", "/especialidades/**")
                    .hasAnyRole("ADMINISTRADOR", "GERENTE", "RECEPCIONISTA")

                // ════════════════════════════════════════════════════════════
                //  ADMISIÓN + TRIAJE — ADMIN + GERENTE + ENFERMERA
                // ════════════════════════════════════════════════════════════
                .requestMatchers("/admision/**").hasAnyRole("ADMINISTRADOR", "GERENTE", "ENFERMERA")
                .requestMatchers("/triaje/api/**").hasAnyRole("ADMINISTRADOR", "GERENTE", "ENFERMERA", "MEDICO")
                .requestMatchers("/triaje/**").hasAnyRole("ADMINISTRADOR", "GERENTE", "ENFERMERA")

                // ════════════════════════════════════════════════════════════
                //  PORTAL PACIENTE — solo PACIENTE
                // ════════════════════════════════════════════════════════════
                .requestMatchers("/portal/**").hasRole("PACIENTE")

                // ════════════════════════════════════════════════════════════
                //  CITAS — todos los roles internos + paciente + enfermera
                // ════════════════════════════════════════════════════════════
                .requestMatchers("/citas/**").hasAnyRole("ADMINISTRADOR", "GERENTE", "MEDICO", "RECEPCIONISTA", "ENFERMERA", "PACIENTE")

                // ════════════════════════════════════════════════════════════
                //  PAGOS — ADMIN + GERENTE + RECEPCIONISTA + ENFERMERA
                // ════════════════════════════════════════════════════════════
                .requestMatchers("/pagos/**").hasAnyRole("ADMINISTRADOR", "GERENTE", "RECEPCIONISTA", "ENFERMERA")

                // ════════════════════════════════════════════════════════════
                //  MÓDULOS MÉDICOS — ADMIN + GERENTE + MEDICO
                // ════════════════════════════════════════════════════════════
                .requestMatchers(
                    "/atenciones/**", "/recetas/**",
                    "/diagnosticos/**", "/historia-clinica/**",
                    "/ordenes-examen/**"
                ).hasAnyRole("ADMINISTRADOR", "GERENTE", "MEDICO")

                .requestMatchers("/medico/**").hasRole("MEDICO")

                // ════════════════════════════════════════════════════════════
                //  CUALQUIER OTRA RUTA — autenticado
                // ════════════════════════════════════════════════════════════
                .anyRequest().authenticated()
            )
            .exceptionHandling(ex -> ex
                .defaultAuthenticationEntryPointFor(
                    (request, response, authException) -> {
                        response.setContentType("application/json");
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.getWriter().write("{\"error\":\"No autenticado\"}");
                    },
                    new AntPathRequestMatcher("/api/**")
                )
            )
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .successHandler(successHandler)
                .failureHandler(failureHandler)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
            )
            .authenticationManager(authenticationManager)
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterAfter(rateLimitingFilter, JwtAuthFilter.class);

        return http.build();
    }
}
