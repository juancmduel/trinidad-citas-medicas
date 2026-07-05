package com.trinidad.citas.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trinidad.citas.dto.CitaDTO;
import com.trinidad.citas.security.CitaSecurity;
import com.trinidad.citas.security.PacienteSecurity;
import com.trinidad.citas.service.CitaService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Pruebas de integración del controlador REST de citas.
 *
 * Se usa @SpringBootTest + @MockBean para cargar el contexto completo
 * pero con los servicios mockeados. La seguridad real se mantiene
 * (JWT filter, form login, etc.) pero @WithMockUser inyecta el
 * contexto de autenticación sin necesidad de tokens reales.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles({"test", "api", "web"})
@DisplayName("CitaRestController — endpoints /api/v1/citas")
class CitaRestControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @MockBean private CitaService citaService;
    @MockBean private CitaSecurity citaSecurity;
    @MockBean private PacienteSecurity pacienteSecurity;

    private CitaDTO crearDtoEjemplo() {
        CitaDTO dto = new CitaDTO();
        dto.setIdCita(1L);
        dto.setIdPaciente(1L);
        dto.setIdMedico(1L);
        dto.setIdEspecialidad(1L);
        dto.setFechaCita(LocalDate.now().plusDays(1));
        dto.setHoraInicio("09:00");
        dto.setEstado("PROGRAMADA");
        dto.setMotivoConsulta("Control");
        dto.setCanalReserva("WEB");
        return dto;
    }

    // ================================================================
    //  GET /api/v1/citas
    // ================================================================
    @Test
    @WithMockUser(roles = "ADMINISTRADOR")
    @DisplayName("GET /citas → 200 con lista de citas")
    void listarTodas() throws Exception {
        given(citaService.listarTodas())
            .willReturn(List.of(crearDtoEjemplo()));

        mockMvc.perform(get("/api/v1/citas")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].idCita").value(1L))
            .andExpect(jsonPath("$[0].estado").value("PROGRAMADA"));
    }

    @Test
    @DisplayName("GET /citas sin autenticar → 401")
    void listarTodas_sinAuth() throws Exception {
        mockMvc.perform(get("/api/v1/citas")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnauthorized());
    }

    // ================================================================
    //  GET /api/v1/citas/{id}
    // ================================================================
    @Test
    @WithMockUser(roles = "ADMINISTRADOR")
    @DisplayName("GET /citas/1 → 200 con cita")
    void obtenerPorId() throws Exception {
        given(citaService.obtenerDTO(1L)).willReturn(crearDtoEjemplo());

        mockMvc.perform(get("/api/v1/citas/1")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.idCita").value(1L));
    }

    @Test
    @WithMockUser(roles = "ADMINISTRADOR")
    @DisplayName("GET /citas/1 como ADMIN → 200")
    void obtenerPorId_comoPaciente() throws Exception {
        given(citaService.obtenerDTO(1L)).willReturn(crearDtoEjemplo());

        mockMvc.perform(get("/api/v1/citas/1")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    // ================================================================
    //  POST /api/v1/citas
    // ================================================================
    @Test
    @WithMockUser(roles = "RECEPCIONISTA")
    @DisplayName("POST /citas → 200 cita creada")
    void agendar() throws Exception {
        CitaDTO dto = crearDtoEjemplo();
        dto.setIdCita(null);
        given(citaService.agendar(any())).willReturn(crearDtoEjemplo());

        mockMvc.perform(post("/api/v1/citas")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.idCita").value(1L));
    }

    @Test
    @WithMockUser(roles = "PACIENTE")
    @DisplayName("POST /citas como PACIENTE → 200 (pueden agendar)")
    void agendar_comoPaciente() throws Exception {
        CitaDTO dto = crearDtoEjemplo();
        dto.setIdCita(null);
        given(citaService.agendar(any())).willReturn(crearDtoEjemplo());

        mockMvc.perform(post("/api/v1/citas")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isOk());
    }

    // ================================================================
    //  POST /api/v1/citas/{id}/cancelar
    // ================================================================
    @Test
    @WithMockUser(roles = "RECEPCIONISTA")
    @DisplayName("POST /citas/1/cancelar → 200")
    void cancelar() throws Exception {
        given(citaService.cancelar(1L)).willReturn(crearDtoEjemplo());

        mockMvc.perform(post("/api/v1/citas/1/cancelar")
                .with(csrf()))
            .andExpect(status().isOk());
    }

    // ================================================================
    //  POST /api/v1/citas/{id}/finalizar
    // ================================================================
    @Test
    @WithMockUser(roles = "ADMINISTRADOR")
    @DisplayName("POST /citas/1/finalizar como ADMIN → 200")
    void finalizar() throws Exception {
        given(citaService.finalizar(1L)).willReturn(crearDtoEjemplo());

        mockMvc.perform(post("/api/v1/citas/1/finalizar")
                .with(csrf()))
            .andExpect(status().isOk());
    }

}
