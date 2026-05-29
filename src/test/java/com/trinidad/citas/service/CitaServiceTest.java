package com.trinidad.citas.service;

import com.trinidad.citas.dto.CitaDTO;
import com.trinidad.citas.exception.BusinessException;
import com.trinidad.citas.exception.ResourceNotFoundException;
import com.trinidad.citas.model.*;
import com.trinidad.citas.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias de CitaService.
 * Semana 17–18: cobertura de reglas de negocio RN-07 y RN-10.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("CitaService — pruebas unitarias")
class CitaServiceTest {

    @Mock private CitaRepository citaRepository;
    @Mock private PacienteRepository pacienteRepository;
    @Mock private MedicoRepository medicoRepository;
    @Mock private EspecialidadRepository especialidadRepository;

    @InjectMocks
    private CitaService citaService;

    private Paciente paciente;
    private Medico medico;
    private Especialidad especialidad;
    private CitaDTO dto;

    @BeforeEach
    void setUp() {
        especialidad = Especialidad.builder()
            .idEspecialidad(1L).nombre("Medicina General")
            .precioConsulta(new BigDecimal("50.00")).duracionMinutos(20).activo(1)
            .build();

        paciente = Paciente.builder()
            .idPaciente(1L).dni("70123456").nombres("Juan")
            .apellidoPaterno("Perez").activo(1)
            .build();

        medico = Medico.builder()
            .idMedico(1L).cmp("12345").nombres("Carlos")
            .apellidoPaterno("Garcia").especialidad(especialidad).activo(1)
            .build();

        dto = new CitaDTO();
        dto.setIdPaciente(1L);
        dto.setIdMedico(1L);
        dto.setIdEspecialidad(1L);
        dto.setFechaCita(LocalDate.now().plusDays(3));
        dto.setHoraInicio("09:00");
        dto.setCanalReserva("WEB");
    }

    // ─── listarTodas ───────────────────────────────────────────────

    @Test
    @DisplayName("listarTodas() retorna lista vacía cuando no hay citas")
    void listarTodas_debeRetornarListaVacia() {
        when(citaRepository.findAll()).thenReturn(Collections.emptyList());

        List<CitaDTO> result = citaService.listarTodas();

        assertThat(result).isEmpty();
        verify(citaRepository, times(1)).findAll();
    }

    // ─── obtenerDTO ────────────────────────────────────────────────

    @Test
    @DisplayName("obtenerDTO() lanza excepción cuando la cita no existe")
    void obtenerDTO_noExiste_lanzaExcepcion() {
        when(citaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> citaService.obtenerDTO(99L))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessageContaining("99");
    }

    @Test
    @DisplayName("obtenerDTO() retorna DTO correcto cuando la cita existe")
    void obtenerDTO_existe_retornaDTO() {
        Cita cita = buildCita(1L, EstadoCita.PROGRAMADA);
        when(citaRepository.findById(1L)).thenReturn(Optional.of(cita));

        CitaDTO result = citaService.obtenerDTO(1L);

        assertThat(result.getIdCita()).isEqualTo(1L);
        assertThat(result.getEstado()).isEqualTo("PROGRAMADA");
    }

    // ─── agendar (RN-07 solapamiento) ──────────────────────────────

    @Test
    @DisplayName("agendar() falla con RN-07 cuando hay solapamiento de horario")
    void agendar_solapamiento_lanzaBusinessException() {
        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));
        when(medicoRepository.findById(1L)).thenReturn(Optional.of(medico));
        when(especialidadRepository.findById(1L)).thenReturn(Optional.of(especialidad));

        // El médico ya tiene una cita a las 09:00
        Cita citaExistente = buildCita(99L, EstadoCita.PROGRAMADA);
        when(citaRepository.findByMedico_IdMedicoAndFechaCitaAndHoraInicio(
            1L, dto.getFechaCita(), "09:00"))
            .thenReturn(Optional.of(citaExistente));

        assertThatThrownBy(() -> citaService.agendar(dto))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("ya tiene una cita");
    }

    @Test
    @DisplayName("agendar() falla con RN-10 cuando el paciente ya tiene cita activa con ese médico en 7 días")
    void agendar_citaActivaReciente_lanzaBusinessException() {
        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));
        when(medicoRepository.findById(1L)).thenReturn(Optional.of(medico));
        when(especialidadRepository.findById(1L)).thenReturn(Optional.of(especialidad));

        when(citaRepository.findByMedico_IdMedicoAndFechaCitaAndHoraInicio(
            anyLong(), any(), anyString()))
            .thenReturn(Optional.empty());

        // Existe una cita activa reciente
        when(citaRepository.findActivasPacienteMedicoDesde(
            eq(1L), eq(1L), any(LocalDate.class)))
            .thenReturn(List.of(buildCita(100L, EstadoCita.PROGRAMADA)));

        assertThatThrownBy(() -> citaService.agendar(dto))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("cita activa");
    }

    @Test
    @DisplayName("agendar() guarda la cita cuando no hay conflictos")
    void agendar_sinConflictos_guardaCita() {
        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));
        when(medicoRepository.findById(1L)).thenReturn(Optional.of(medico));
        when(especialidadRepository.findById(1L)).thenReturn(Optional.of(especialidad));
        when(citaRepository.findByMedico_IdMedicoAndFechaCitaAndHoraInicio(
            anyLong(), any(), anyString()))
            .thenReturn(Optional.empty());
        when(citaRepository.findActivasPacienteMedicoDesde(
            anyLong(), anyLong(), any()))
            .thenReturn(Collections.emptyList());

        Cita savedCita = buildCita(5L, EstadoCita.PROGRAMADA);
        when(citaRepository.save(any(Cita.class))).thenReturn(savedCita);

        CitaDTO result = citaService.agendar(dto);

        assertThat(result).isNotNull();
        assertThat(result.getIdCita()).isEqualTo(5L);
        verify(citaRepository).save(any(Cita.class));
    }

    // ─── cancelar ─────────────────────────────────────────────────

    @Test
    @DisplayName("cancelar() cambia estado a CANCELADA")
    void cancelar_debePonerEstadoCancelada() {
        Cita cita = buildCita(1L, EstadoCita.PROGRAMADA);
        when(citaRepository.findById(1L)).thenReturn(Optional.of(cita));
        when(citaRepository.save(any(Cita.class))).thenAnswer(inv -> inv.getArgument(0));

        CitaDTO result = citaService.cancelar(1L);

        assertThat(result.getEstado()).isEqualTo("CANCELADA");
        verify(citaRepository).save(argThat(c -> c.getEstado() == EstadoCita.CANCELADA));
    }

    @Test
    @DisplayName("cancelar() lanza excepción cuando la cita no existe")
    void cancelar_noExiste_lanzaExcepcion() {
        when(citaRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> citaService.cancelar(999L))
            .isInstanceOf(ResourceNotFoundException.class);
    }

    // ─── cambiarEstado ─────────────────────────────────────────────

    @Test
    @DisplayName("cambiarEstado() actualiza correctamente a CONFIRMADA")
    void cambiarEstado_aConfirmada_actualizaEstado() {
        Cita cita = buildCita(1L, EstadoCita.PROGRAMADA);
        when(citaRepository.findById(1L)).thenReturn(Optional.of(cita));
        when(citaRepository.save(any(Cita.class))).thenAnswer(inv -> inv.getArgument(0));

        CitaDTO result = citaService.cambiarEstado(1L, EstadoCita.CONFIRMADA);

        assertThat(result.getEstado()).isEqualTo("CONFIRMADA");
    }

    // ─── listarPorFecha ────────────────────────────────────────────

    @Test
    @DisplayName("listarPorFecha() delega correctamente al repositorio")
    void listarPorFecha_delegaAlRepositorio() {
        LocalDate hoy = LocalDate.now();
        when(citaRepository.findByFechaCitaOrderByHoraInicioAsc(hoy))
            .thenReturn(List.of(buildCita(1L, EstadoCita.PROGRAMADA)));

        List<CitaDTO> result = citaService.listarPorFecha(hoy);

        assertThat(result).hasSize(1);
        verify(citaRepository).findByFechaCitaOrderByHoraInicioAsc(hoy);
    }

    // ─── Helper ───────────────────────────────────────────────────

    private Cita buildCita(Long id, EstadoCita estado) {
        return Cita.builder()
            .idCita(id)
            .paciente(paciente)
            .medico(medico)
            .especialidad(especialidad)
            .fechaCita(LocalDate.now().plusDays(3))
            .horaInicio("09:00")
            .horaFin("09:20")
            .estado(estado)
            .canalReserva("WEB")
            .build();
    }
}
