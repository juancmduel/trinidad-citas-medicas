package com.trinidad.citas.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Value("${server.servlet.context-path:}")
    private String contextPath;

    @Bean
    public OpenAPI trinidadOpenAPI() {
        final String securitySchemeName = "bearerAuth";

        return new OpenAPI()
            .info(new Info()
                .title("Trinidad Citas Medicas API")
                .version("1.0.0")
                .description("""
                    API REST del sistema de gestión de citas médicas para 
                    **Trinidad y Especialidades Médicas S.A.C.**
                    
                    ### Roles del sistema
                    - **ADMINISTRADOR** — Acceso total a todas las operaciones
                    - **GERENTE** — Reportes, dashboards y lectura de datos
                    - **MEDICO** — Gestión de citas propias, atenciones, recetas, órdenes
                    - **RECEPCIONISTA** — Admisión de pacientes, registro de citas y pagos
                    - **ENFERMERA** — Triaje, lectura de pacientes y citas
                    - **PACIENTE** — Consulta de datos propios y auto-agendamiento
                    
                    ### Autenticación
                    Todas las rutas `/api/v1/**` requieren token JWT en el header 
                    `Authorization: Bearer <token>`, excepto `/api/auth/**` (login público).
                    """)
                .contact(new Contact()
                    .name("Trinidad y Especialidades Médicas S.A.C.")
                    .email("admin@trinidadtarapoto.com")
                    .url("https://trinidadtarapoto.com"))
                .license(new License()
                    .name("Propietario")
                    .url("https://trinidadtarapoto.com")))
            .servers(List.of(
                new Server()
                    .url("http://localhost:8081" + contextPath)
                    .description("Servidor local de desarrollo"),
                new Server()
                    .url("https://api.trinidadtarapoto.com" + contextPath)
                    .description("Servidor de producción")
            ))
            .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
            .components(new Components()
                .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                    .name(securitySchemeName)
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("bearer")
                    .bearerFormat("JWT")
                    .description("Ingrese el token JWT obtenido en POST /api/auth/login")));
    }
}
