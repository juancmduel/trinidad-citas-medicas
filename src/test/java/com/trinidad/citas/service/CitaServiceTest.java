package com.trinidad.citas.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import org.mockito.junit.jupiter.MockitoExtension;

import com.trinidad.citas.dto.CitaDTO;
import com.trinidad.citas.exception.BusinessException;
import com.trinidad.citas.exception.ResourceNotFoundException;
import com.trinidad.citas.model.Cita;
import com.trinidad.citas.model.Especialidad;
import com.trinidad.citas.model.EstadoCita;
import com.trinidad.citas.model.Medico;
import com.trinidad.citas.model.Paciente;
import com.trinidad.citas.repository.CitaRepository;
import com.trinidad.citas.repository.EspecialidadRepository;
import com.trinidad.citas.repository.MedicoRepository;
import com.trinidad.citas.repository.PacienteRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("CitaService — lógica de negocio de citas")
class CitaServiceTest {

    @Mock private CitaRepository citaRepository;
    @Mock private PacienteRepository pacienteRepository;
    @Mock private MedicoRepository medicoRepository;
    @Mock private EspecialidadRepository especialidadRepository;
    @Mock private EmailService emailService;
    @InjectMocks private CitaService citaService;

    @Captor private ArgumentCaptor<Cita> citaCaptor;

    private Paciente paciente;
    private Medico medico;
    private Especialidad especialidad;
    private CitaDTO dtoValido;

    @BeforeEach
    void setUp() {
        paciente = Paciente.builder()
            .idPaciente(1L).dni("70123456").nombres("Juan")
            .apellidoPaterno("Perez").build();

        especialidad = Especialidad.builder()
            .idEspecialidad(1L).nombre("Medicina General")
            .duracionMinutos(20).build();

        medico = Medico.builder()
            .idMedico(1L).cmp("12345").nombres("Carlos")
            .apellidoPaterno("Garcia").especialidad(especialidad).build();

        dtoValido = new CitaDTO();
        dtoValido.setIdPaciente(1L);
        dtoValido.setIdMedico(1L);
        dtoValido.setIdEspecialidad(1L);
        dtoValido.setFechaCita(LocalDate.now().plusDays(1));
        dtoValido.setHoraInicio("09:00");
        dtoValido.setMotivoConsulta("Control general");
        dtoValido.setCanalReserva("WEB");
    }

    // ============================================================
    //  TEST: agendar()
    // ============================================================
    @Nested
    @DisplayName("agendar(CitaDTO)")
    class Agendar {

        @Test
        @DisplayName("Debe crear una cita cuando los datos son válidos")
        void debeCrearCitaExitosa() {
            // given
            given(pacienteRepository.findById(1L)).willReturn(Optional.of(paciente));
            given(medicoRepository.findById(1L)).willReturn(Optional.of(medico));
            given(especialidadRepository.findById(1L)).willReturn(Optional.of(especialidad));
            given(citaRepository.findByMedico_IdMedicoAndFechaCita(anyLong(), any()))
                .willReturn(List.of()); // sin conflictos
            given(citaRepository.findActivasPacienteMedicoDesde(anyLong(), anyLong(), any()))
                .willReturn(List.of()); // sin citas recientes activas
            given(citaRepository.save(any())).willAnswer(inv -> {
                Cita c = inv.getArgument(0);
                c.setIdCita(100L);
                c.setEstado(EstadoCita.PROGRAMADA);
                return c;
            });

            // when
            CitaDTO resultado = citaService.agendar(dtoValido);

            // then
            assertThat(resultado).isNotNull();
            assertThat(resultado.getIdCita()).isEqualTo(100L);
            assertThat(resultado.getEstado()).isEqualTo("PROGRAMADA");
            then(emailService).should().enviarConfirmacionCita(any());
        }

        @Test
        @DisplayName("Debe lanzar BusinessException si hay conflicto de horario")
        void debeRechazarConflictoHorario() {
            // given
            Cita conflicto = Cita.builder()
                .idCita(50L).horaInicio("08:30").horaFin("09:20").build();

            given(pacienteRepository.findById(1L)).willReturn(Optional.of(paciente));
            given(medicoRepository.findById(1L)).willReturn(Optional.of(medico));
            given(especialidadRepository.findById(1L)).willReturn(Optional.of(especialidad));
            given(citaRepository.findByMedico_IdMedicoAndFechaCita(anyLong(), any()))
                .willReturn(List.of(conflicto));

            // when / then
            assertThatThrownBy(() -> citaService.agendar(dtoValido))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("ya tiene una cita programada");

            then(citaRepository).should(never()).save(any());
            then(emailService).shouldHaveNoInteractions();
        }

        @Test
        @DisplayName("Debe lanzar ResourceNotFoundException si el paciente no existe")
        void debeFallarSiPacienteNoExiste() {
            given(pacienteRepository.findById(anyLong())).willReturn(Optional.empty());

            assertThatThrownBy(() -> citaService.agendar(dtoValido))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Paciente");
        }

        @Test
        @DisplayName("Debe rechazar si el paciente tiene cita activa reciente con el mismo médico")
        void debeRechazarCitaRecienteActiva() {
            Cita reciente = Cita.builder()
                .idCita(99L).fechaCita(LocalDate.now()).horaInicio("10:00")
                .estado(EstadoCita.PROGRAMADA).build();

            given(pacienteRepository.findById(1L)).willReturn(Optional.of(paciente));
            given(medicoRepository.findById(1L)).willReturn(Optional.of(medico));
            given(especialidadRepository.findById(1L)).willReturn(Optional.of(especialidad));
            given(citaRepository.findByMedico_IdMedicoAndFechaCita(anyLong(), any()))
                .willReturn(List.of());
            given(citaRepository.findActivasPacienteMedicoDesde(anyLong(), anyLong(), any()))
                .willReturn(List.of(reciente));

            assertThatThrownBy(() -> citaService.agendar(dtoValido))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("ya tiene una cita activa");
        }
    }

    // ============================================================
    //  TEST: cambiarEstado()
    // ============================================================
    @Nested
    @DisplayName("cambiarEstado(Long, EstadoCita)")
    class CambiarEstado {

        @Test
        @DisplayName("Debe cambiar el estado de una cita existente")
        void debeCambiarEstado() {
            Cita cita = Cita.builder().idCita(1L).estado(EstadoCita.PROGRAMADA).build();
            given(citaRepository.findById(1L)).willReturn(Optional.of(cita));
            given(citaRepository.save(any())).willAnswer(inv -> inv.getArgument(0));

            CitaDTO result = citaService.cambiarEstado(1L, EstadoCita.CONFIRMADA);

            assertThat(result.getEstado()).isEqualTo("CONFIRMADA");
        }

        @Test
        @DisplayName("Debe lanzar excepción si la cita no existe")
        void debeFallarSiNoExiste() {
            given(citaRepository.findById(anyLong())).willReturn(Optional.empty());
            assertThatThrownBy(() -> citaService.cambiarEstado(999L, EstadoCita.CANCELADA))
                .isInstanceOf(ResourceNotFoundException.class);
        }
    }

    // ============================================================
    //  TEST: cancelar()
    // ============================================================
    @Test
    @DisplayName("cancelar() debe cambiar estado a CANCELADA")
    void cancelar() {
        Cita cita = Cita.builder().idCita(1L).estado(EstadoCita.PROGRAMADA).build();
        given(citaRepository.findById(1L)).willReturn(Optional.of(cita));
        given(citaRepository.save(any())).willAnswer(inv -> inv.getArgument(0));

        CitaDTO result = citaService.cancelar(1L);

        assertThat(result.getEstado()).isEqualTo("CANCELADA");
    }

    // ============================================================
    //  TEST: confirmarPago()
    // ============================================================
    @Test
    @DisplayName("confirmarPago() solo funciona en estado PROGRAMADA")
    void confirmarPago() {
        Cita cita = Cita.builder().idCita(1L).estado(EstadoCita.PROGRAMADA).build();
        given(citaRepository.findById(1L)).willReturn(Optional.of(cita));
        given(citaRepository.save(any())).willAnswer(inv -> inv.getArgument(0));

        CitaDTO result = citaService.confirmarPago(1L);

        assertThat(result.getEstado()).isEqualTo("CONFIRMADA");
    }

    @Test
    @DisplayName("confirmarPago() debe rechazar cita no PROGRAMADA")
    void confirmarPago_conEstadoIncorrecto() {
        Cita cita = Cita.builder().idCita(1L).estado(EstadoCita.CONFIRMADA).build();
        given(citaRepository.findById(1L)).willReturn(Optional.of(cita));

        assertThatThrownBy(() -> citaService.confirmarPago(1L))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("PROGRAMADA");
    }

    // ============================================================
    //  TEST: checkin()
    // ============================================================
    @Test
    @DisplayName("checkin() solo funciona en estado CONFIRMADA")
    void checkin() {
        Cita cita = Cita.builder().idCita(1L).estado(EstadoCita.CONFIRMADA)
            .fechaCheckin(null).build();
        given(citaRepository.findById(1L)).willReturn(Optional.of(cita));
        given(citaRepository.save(any())).willAnswer(inv -> inv.getArgument(0));

        CitaDTO result = citaService.checkin(1L);

        assertThat(result.getEstado()).isEqualTo("EN_TRIAGE");
    }

    // ============================================================
    //  TEST: finalizar()
    // ============================================================
    @Test
    @DisplayName("finalizar() solo funciona en estado EN_ATENCION")
    void finalizar() {
        Cita cita = Cita.builder().idCita(1L).estado(EstadoCita.EN_ATENCION).build();
        given(citaRepository.findById(1L)).willReturn(Optional.of(cita));
        given(citaRepository.save(any())).willAnswer(inv -> inv.getArgument(0));

        CitaDTO result = citaService.finalizar(1L);

        assertThat(result.getEstado()).isEqualTo("ATENDIDA");
    }
}
