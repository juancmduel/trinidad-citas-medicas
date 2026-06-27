package com.trinidad.citas.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
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
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
        http
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
                    "/api/auth/**", "/api/v1/auth/**"
                ).permitAll()

                // ════════════════════════════════════════════════════════════
                //  API REST — cualquier usuario autenticado
                // ════════════════════════════════════════════════════════════
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
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
