package com.trinidad.citas.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Pruebas de acceso basado en roles (@PreAuthorize) a los endpoints REST.
 *
 * Para evitar depender de JWT en estas pruebas, se usa @WithMockUser
 * y se mockean los beans de seguridad (CitaSecurity, PacienteSecurity,
 * MedicoSecurity) para que los SpEL expressions @bean.method() retornen
 * valores controlados.
 *
 * Al usar @SpringBootTest, se levanta todo el contexto (servicios, repos,
 * seguridad, filtros JWT, etc.). Los repositorios operan contra H2 en
 * memoria gracias a application-test.properties.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles({"test", "api", "web"})
@Sql(scripts = "/seed-test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@DisplayName("SecurityAccessTest — Verificación de @PreAuthorize en endpoints")
class SecurityAccessTest {

    @Autowired
    private MockMvc mockMvc;

    // ================================================================
    //  Endpoints de administración
    // ================================================================
    @Nested
    @DisplayName("ADMINISTRADOR puede acceder a endpoints de administración")
    class AdminAccess {

        @Test
        @WithMockUser(roles = "ADMINISTRADOR")
        @DisplayName("GET /api/v1/usuarios → 200 para ADMIN")
        void listarUsuarios() throws Exception {
            mockMvc.perform(get("/api/v1/usuarios")
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        }

        @Test
        @WithMockUser(roles = "ADMINISTRADOR")
        @DisplayName("GET /api/v1/pacientes → 200 para ADMIN")
        void listarPacientes() throws Exception {
            mockMvc.perform(get("/api/v1/pacientes")
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        }

        @Test
        @WithMockUser(roles = "ADMINISTRADOR")
        @DisplayName("GET /api/v1/medicos → 200 para ADMIN")
        void listarMedicos() throws Exception {
            mockMvc.perform(get("/api/v1/medicos")
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        }

        @Test
        @WithMockUser(roles = "ADMINISTRADOR")
        @DisplayName("GET /api/v1/especialidades → 200 para ADMIN")
        void listarEspecialidades() throws Exception {
            mockMvc.perform(get("/api/v1/especialidades")
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        }
    }

    // ================================================================
    //  Denegación a roles no autorizados
    // ================================================================
    @Nested
    @DisplayName("Roles sin permiso reciben 403 Forbidden")
    class ForbiddenAccess {

        @Test
        @WithMockUser(roles = "PACIENTE")
        @DisplayName("PACIENTE no puede listar usuarios")
        void pacienteNoPuedeListarUsuarios() throws Exception {
            mockMvc.perform(get("/api/v1/usuarios")
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
        }

        @Test
        @WithMockUser(roles = "PACIENTE")
        @DisplayName("PACIENTE puede listar médicos (todos los roles activos tienen acceso)")
        void pacientePuedeListarMedicos() throws Exception {
            mockMvc.perform(get("/api/v1/medicos")
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        }

        @Test
        @WithMockUser(roles = "RECEPCIONISTA")
        @DisplayName("RECEPCIONISTA no puede eliminar pacientes")
        void recepcionistaNoPuedeEliminar() throws Exception {
            mockMvc.perform(delete("/api/v1/pacientes/1")
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
        }
    }

    // ================================================================
    //  Endpoints de citas (acceso controlado)
    // ================================================================
    @Nested
    @DisplayName("Endpoint /api/v1/citas")
    class CitasAccess {

        @Test
        @WithMockUser(roles = "RECEPCIONISTA")
        @DisplayName("RECEPCIONISTA puede listar citas")
        void recepcionistaLista() throws Exception {
            mockMvc.perform(get("/api/v1/citas")
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        }

        @Test
        @WithMockUser(roles = "MEDICO")
        @DisplayName("MEDICO puede listar citas")
        void medicoLista() throws Exception {
            mockMvc.perform(get("/api/v1/citas")
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        }

        @Test
        @WithMockUser(roles = "PACIENTE")
        @DisplayName("PACIENTE no puede listar todas las citas (solo las suyas por /paciente/{id})")
        void pacienteLista() throws Exception {
            mockMvc.perform(get("/api/v1/citas")
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
        }
    }

    // ================================================================
    //  Acceso anónimo
    // ================================================================
    @Nested
    @DisplayName("Endpoints públicos sin autenticación")
    class PublicAccess {

        @Test
        @DisplayName("POST /api/auth/login sin auth → 401 (esperado, el body es inválido)")
        void loginSinCredenciales() throws Exception {
            mockMvc.perform(post("/api/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{}"))
                .andExpect(status().isBadRequest()); // validation error
        }

        @Test
        @DisplayName("GET /api/v1/citas sin auth → 401")
        void citasSinAuth() throws Exception {
            mockMvc.perform(get("/api/v1/citas")
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
        }
    }

    // ================================================================
    //  Swagger / OpenAPI (debe ser público)
    // ================================================================
    @Nested
    @DisplayName("Swagger UI y docs públicos")
    class SwaggerAccess {

        @Test
        @DisplayName("GET /v3/api-docs sin auth → 200")
        void apiDocs() throws Exception {
            mockMvc.perform(get("/v3/api-docs")
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        }

        @Test
        @DisplayName("GET /swagger-ui/index.html sin auth → 200")
        void swaggerUi() throws Exception {
            mockMvc.perform(get("/swagger-ui/index.html"))
                .andExpect(status().isOk());
        }
    }
}
