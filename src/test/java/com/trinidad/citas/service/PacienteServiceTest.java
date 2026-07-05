package com.trinidad.citas.service;

import com.trinidad.citas.dto.PacienteDTO;
import com.trinidad.citas.exception.DuplicateResourceException;
import com.trinidad.citas.exception.ResourceNotFoundException;
import com.trinidad.citas.model.Paciente;
import com.trinidad.citas.repository.PacienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
@DisplayName("PacienteService — CRUD de pacientes")
class PacienteServiceTest {

    @Mock private PacienteRepository pacienteRepository;
    @InjectMocks private PacienteService pacienteService;

    private PacienteDTO dtoValido;

    @BeforeEach
    void setUp() {
        dtoValido = new PacienteDTO();
        dtoValido.setDni("70123456");
        dtoValido.setNombres("Juan");
        dtoValido.setApellidoPaterno("Perez");
        dtoValido.setFechaNacimiento(LocalDate.of(1990, 5, 15));
        dtoValido.setSexo("M");
    }

    @Nested
    @DisplayName("crear(PacienteDTO)")
    class Crear {

        @Test
        @DisplayName("Debe crear un paciente con datos válidos")
        void debeCrear() {
            given(pacienteRepository.existsByDni("70123456")).willReturn(false);
            given(pacienteRepository.save(any())).willAnswer(inv -> {
                Paciente p = inv.getArgument(0);
                p.setIdPaciente(1L);
                return p;
            });

            PacienteDTO result = pacienteService.crear(dtoValido);

            assertThat(result).isNotNull();
            assertThat(result.getIdPaciente()).isEqualTo(1L);
            assertThat(result.getDni()).isEqualTo("70123456");
        }

        @Test
        @DisplayName("Debe rechazar DNI duplicado")
        void debeRechazarDniDuplicado() {
            given(pacienteRepository.existsByDni("70123456")).willReturn(true);

            assertThatThrownBy(() -> pacienteService.crear(dtoValido))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("DNI");

            then(pacienteRepository).should(never()).save(any());
        }
    }

    @Nested
    @DisplayName("actualizar(Long, PacienteDTO)")
    class Actualizar {

        @Test
        @DisplayName("Debe actualizar un paciente existente")
        void debeActualizar() {
            Paciente existente = Paciente.builder()
                .idPaciente(1L).dni("70123456").nombres("Juan").build();
            given(pacienteRepository.findById(1L)).willReturn(Optional.of(existente));
            given(pacienteRepository.save(any())).willAnswer(inv -> inv.getArgument(0));

            dtoValido.setNombres("Juan Actualizado");
            PacienteDTO result = pacienteService.actualizar(1L, dtoValido);

            assertThat(result.getNombres()).isEqualTo("Juan Actualizado");
        }

        @Test
        @DisplayName("Debe fallar si el paciente no existe")
        void debeFallarSiNoExiste() {
            given(pacienteRepository.findById(any())).willReturn(Optional.empty());
            assertThatThrownBy(() -> pacienteService.actualizar(999L, dtoValido))
                .isInstanceOf(ResourceNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("eliminar(Long)")
    class Eliminar {

        @Test
        @DisplayName("Debe hacer soft-delete (activo=0)")
        void debeSoftDelete() {
            Paciente p = Paciente.builder().idPaciente(1L).dni("70123456")
                .activo(1).build();
            given(pacienteRepository.findById(1L)).willReturn(Optional.of(p));
            given(pacienteRepository.save(any())).willAnswer(inv -> inv.getArgument(0));

            pacienteService.eliminar(1L);

            assertThat(p.getActivo()).isZero();
        }

        @Test
        @DisplayName("Debe fallar si el paciente no existe")
        void debeFallarSiNoExiste() {
            given(pacienteRepository.findById(any())).willReturn(Optional.empty());
            assertThatThrownBy(() -> pacienteService.eliminar(999L))
                .isInstanceOf(ResourceNotFoundException.class);
        }
    }
}
