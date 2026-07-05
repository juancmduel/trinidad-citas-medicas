package com.trinidad.citas.service;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.trinidad.citas.dto.AtencionDTO;
import com.trinidad.citas.exception.BusinessException;
import com.trinidad.citas.exception.ResourceNotFoundException;
import com.trinidad.citas.model.Atencion;
import com.trinidad.citas.model.Cita;
import com.trinidad.citas.model.EstadoCita;
import com.trinidad.citas.model.HistoriaClinica;
import com.trinidad.citas.model.Medico;
import com.trinidad.citas.model.Paciente;
import com.trinidad.citas.model.Pago;
import com.trinidad.citas.repository.AtencionRepository;
import com.trinidad.citas.repository.CitaRepository;
import com.trinidad.citas.repository.DiagnosticoCie10Repository;
import com.trinidad.citas.repository.HistoriaClinicaRepository;
import com.trinidad.citas.repository.MedicoRepository;
import com.trinidad.citas.repository.PagoRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("AtencionService — registro de atenciones médicas")
class AtencionServiceTest {

    @Mock private AtencionRepository atencionRepository;
    @Mock private CitaRepository citaRepository;
    @Mock private HistoriaClinicaRepository historiaClinicaRepository;
    @Mock private MedicoRepository medicoRepository;
    @Mock private DiagnosticoCie10Repository diagnosticoCie10Repository;
    @Mock private PagoRepository pagoRepository;

    @InjectMocks private AtencionService atencionService;

    private Cita citaPendiente;
    private Pago pagoConfirmado;
    private AtencionDTO dtoValido;

    @BeforeEach
    void setUp() {
        citaPendiente = new Cita();
        citaPendiente.setIdCita(1L);
        citaPendiente.setEstado(EstadoCita.EN_ATENCION);

        pagoConfirmado = new Pago();
        pagoConfirmado.setEstado("PAGADO");

        dtoValido = new AtencionDTO();
        dtoValido.setIdCita(1L);
        dtoValido.setIdHistoria(1L);
        dtoValido.setIdMedico(1L);
        dtoValido.setMotivoConsulta("Control");
        dtoValido.setAnamnesis("Paciente refiere dolor de cabeza");
        dtoValido.setDiagnosticoDesc("Cefalea tensional");
        dtoValido.setTratamiento("Paracetamol 500mg c/8h");
    }

    @Nested
    @DisplayName("crear(AtencionDTO)")
    class Crear {

        @Test
        @DisplayName("Debe crear atención con datos válidos")
        void debeCrear() {
            given(citaRepository.findById(1L)).willReturn(Optional.of(citaPendiente));
            given(pagoRepository.findByCita_IdCita(1L)).willReturn(Optional.of(pagoConfirmado));
            given(historiaClinicaRepository.findById(1L))
                .willReturn(Optional.of(new HistoriaClinica()));
            given(medicoRepository.findById(1L))
                .willReturn(Optional.of(Medico.builder().idMedico(1L).build()));
            given(atencionRepository.save(any())).willAnswer(inv -> {
                Atencion a = inv.getArgument(0);
                a.setIdAtencion(1L);
                return a;
            });

            AtencionDTO result = atencionService.crear(dtoValido);

            assertThat(result).isNotNull();
            assertThat(result.getIdAtencion()).isEqualTo(1L);
            then(citaRepository).should().save(any());
        }

        @Test
        @DisplayName("Debe fallar si la cita no existe")
        void debeFallarCitaNoExiste() {
            given(citaRepository.findById(any())).willReturn(Optional.empty());
            assertThatThrownBy(() -> atencionService.crear(dtoValido))
                .isInstanceOf(ResourceNotFoundException.class);
        }

        @Test
        @DisplayName("Debe fallar si la cita no está en EN_ATENCION")
        void debeFallarCitaNoEnAtencion() {
            citaPendiente.setEstado(EstadoCita.PROGRAMADA);
            given(citaRepository.findById(1L)).willReturn(Optional.of(citaPendiente));

            assertThatThrownBy(() -> atencionService.crear(dtoValido))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("EN_ATENCION");
        }

        @Test
        @DisplayName("Debe fallar si el pago no está confirmado")
        void debeFallarPagoNoConfirmado() {
            given(citaRepository.findById(1L)).willReturn(Optional.of(citaPendiente));
            given(pagoRepository.findByCita_IdCita(1L)).willReturn(Optional.empty());

            assertThatThrownBy(() -> atencionService.crear(dtoValido))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("pago");
        }

        @Test
        @DisplayName("Debe fallar si el pago no está en estado PAGADO")
        void debeFallarPagoEstadoIncorrecto() {
            pagoConfirmado.setEstado("PENDIENTE");
            given(citaRepository.findById(1L)).willReturn(Optional.of(citaPendiente));
            given(pagoRepository.findByCita_IdCita(1L)).willReturn(Optional.of(pagoConfirmado));

            assertThatThrownBy(() -> atencionService.crear(dtoValido))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("confirmado");
        }
    }

    @Nested
    @DisplayName("obtenerPorId(Long)")
    class ObtenerPorId {

        @Test
        @DisplayName("Debe retornar atención existente")
        void debeRetornar() {
            HistoriaClinica hc = new HistoriaClinica();
            hc.setIdHistoria(1L);
            Atencion atencion = new Atencion();
            atencion.setIdAtencion(1L);
            atencion.setHistoria(hc);
            var cita = new Cita();
            cita.setPaciente(Paciente.builder().nombres("Juan").apellidoPaterno("Perez").build());
            atencion.setCita(cita);
            atencion.setMedico(Medico.builder().nombres("Gregory").apellidoPaterno("House").build());

            given(atencionRepository.findById(1L)).willReturn(Optional.of(atencion));

            AtencionDTO result = atencionService.obtenerPorId(1L);
            assertThat(result.getIdAtencion()).isEqualTo(1L);
        }

        @Test
        @DisplayName("Debe fallar si no existe")
        void debeFallarNoExiste() {
            given(atencionRepository.findById(any())).willReturn(Optional.empty());
            assertThatThrownBy(() -> atencionService.obtenerPorId(999L))
                .isInstanceOf(ResourceNotFoundException.class);
        }
    }
}
