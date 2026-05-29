package com.trinidad.citas.service;

import com.trinidad.citas.dto.PacienteDTO;
import com.trinidad.citas.exception.DuplicateResourceException;
import com.trinidad.citas.exception.ResourceNotFoundException;
import com.trinidad.citas.model.Paciente;
import com.trinidad.citas.repository.PacienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias de PacienteService.
 * Semana 17–18: cobertura de RN-02 (unicidad DNI) y CRUD completo.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("PacienteService — pruebas unitarias")
class PacienteServiceTest {

    @Mock
    private PacienteRepository pacienteRepository;

    @InjectMocks
    private PacienteService pacienteService;

    private Paciente pacienteExistente;
    private PacienteDTO dtoNuevo;

    @BeforeEach
    void setUp() {
        pacienteExistente = Paciente.builder()
            .idPaciente(1L)
            .dni("70123456")
            .nombres("Juan")
            .apellidoPaterno("Perez")
            .apellidoMaterno("Quispe")
            .fechaNacimiento(LocalDate.of(1990, 5, 15))
            .sexo("M")
            .telefono("942555666")
            .activo(1)
            .build();

        dtoNuevo = new PacienteDTO();
        dtoNuevo.setDni("70999888");
        dtoNuevo.setNombres("Ana");
        dtoNuevo.setApellidoPaterno("Lopez");
        dtoNuevo.setFechaNacimiento(LocalDate.of(1995, 3, 20));
        dtoNuevo.setSexo("F");
    }

    // ─── listarTodos ──────────────────────────────────────────────

    @Test
    @DisplayName("listarTodos() retorna todos los pacientes mapeados como DTO")
    void listarTodos_retornaListaDeDTO() {
        when(pacienteRepository.findAll()).thenReturn(List.of(pacienteExistente));

        List<PacienteDTO> result = pacienteService.listarTodos();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getDni()).isEqualTo("70123456");
        assertThat(result.get(0).getNombres()).isEqualTo("Juan");
    }

    @Test
    @DisplayName("listarTodos() retorna lista vacía cuando no hay pacientes")
    void listarTodos_sinPacientes_retornaVacio() {
        when(pacienteRepository.findAll()).thenReturn(Collections.emptyList());

        assertThat(pacienteService.listarTodos()).isEmpty();
    }

    // ─── obtenerDTO ───────────────────────────────────────────────

    @Test
    @DisplayName("obtenerDTO() retorna DTO cuando el paciente existe")
    void obtenerDTO_existe_retornaDTO() {
        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(pacienteExistente));

        PacienteDTO result = pacienteService.obtenerDTO(1L);

        assertThat(result.getIdPaciente()).isEqualTo(1L);
        assertThat(result.getDni()).isEqualTo("70123456");
    }

    @Test
    @DisplayName("obtenerDTO() lanza ResourceNotFoundException cuando no existe")
    void obtenerDTO_noExiste_lanzaExcepcion() {
        when(pacienteRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> pacienteService.obtenerDTO(999L))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessageContaining("999");
    }

    // ─── obtenerPorDni ────────────────────────────────────────────

    @Test
    @DisplayName("obtenerPorDni() retorna DTO cuando el DNI existe")
    void obtenerPorDni_existe_retornaDTO() {
        when(pacienteRepository.findByDni("70123456")).thenReturn(Optional.of(pacienteExistente));

        PacienteDTO result = pacienteService.obtenerPorDni("70123456");

        assertThat(result.getDni()).isEqualTo("70123456");
    }

    @Test
    @DisplayName("obtenerPorDni() lanza excepción cuando el DNI no existe")
    void obtenerPorDni_noExiste_lanzaExcepcion() {
        when(pacienteRepository.findByDni("00000000")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> pacienteService.obtenerPorDni("00000000"))
            .isInstanceOf(ResourceNotFoundException.class);
    }

    // ─── crear (RN-02) ────────────────────────────────────────────

    @Test
    @DisplayName("crear() guarda paciente cuando el DNI no está duplicado (RN-02)")
    void crear_dniNuevo_guardaCorrectamente() {
        when(pacienteRepository.existsByDni("70999888")).thenReturn(false);
        Paciente saved = Paciente.builder()
            .idPaciente(2L).dni("70999888").nombres("Ana")
            .apellidoPaterno("Lopez").fechaNacimiento(LocalDate.of(1995, 3, 20))
            .sexo("F").activo(1).build();
        when(pacienteRepository.save(any(Paciente.class))).thenReturn(saved);

        PacienteDTO result = pacienteService.crear(dtoNuevo);

        assertThat(result.getIdPaciente()).isEqualTo(2L);
        assertThat(result.getDni()).isEqualTo("70999888");
        verify(pacienteRepository).save(any(Paciente.class));
    }

    @Test
    @DisplayName("crear() lanza DuplicateResourceException si el DNI ya existe (RN-02)")
    void crear_dniDuplicado_lanzaExcepcion() {
        when(pacienteRepository.existsByDni("70999888")).thenReturn(true);

        assertThatThrownBy(() -> pacienteService.crear(dtoNuevo))
            .isInstanceOf(DuplicateResourceException.class)
            .hasMessageContaining("70999888");

        verify(pacienteRepository, never()).save(any());
    }

    // ─── actualizar ───────────────────────────────────────────────

    @Test
    @DisplayName("actualizar() guarda cambios cuando el DNI no cambia")
    void actualizar_mismoDni_actualizaCorrectamente() {
        dtoNuevo.setDni("70123456"); // mismo DNI que el existente
        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(pacienteExistente));
        when(pacienteRepository.save(any(Paciente.class))).thenReturn(pacienteExistente);

        PacienteDTO result = pacienteService.actualizar(1L, dtoNuevo);

        assertThat(result).isNotNull();
        verify(pacienteRepository).save(any(Paciente.class));
    }

    @Test
    @DisplayName("actualizar() lanza excepción si el nuevo DNI pertenece a otro paciente (RN-02)")
    void actualizar_nuevoDniDuplicado_lanzaExcepcion() {
        dtoNuevo.setDni("70999777"); // DNI diferente que ya existe en BD
        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(pacienteExistente));
        when(pacienteRepository.existsByDni("70999777")).thenReturn(true);

        assertThatThrownBy(() -> pacienteService.actualizar(1L, dtoNuevo))
            .isInstanceOf(DuplicateResourceException.class);

        verify(pacienteRepository, never()).save(any());
    }

    // ─── eliminar (borrado lógico) ─────────────────────────────────

    @Test
    @DisplayName("eliminar() pone activo=0 en lugar de borrar físicamente")
    void eliminar_desactivaPaciente() {
        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(pacienteExistente));
        when(pacienteRepository.save(any(Paciente.class))).thenAnswer(inv -> inv.getArgument(0));

        pacienteService.eliminar(1L);

        verify(pacienteRepository).save(argThat(p -> p.getActivo() == 0));
    }

    @Test
    @DisplayName("eliminar() lanza excepción cuando el paciente no existe")
    void eliminar_noExiste_lanzaExcepcion() {
        when(pacienteRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> pacienteService.eliminar(999L))
            .isInstanceOf(ResourceNotFoundException.class);
    }

    // ─── buscar ───────────────────────────────────────────────────

    @Test
    @DisplayName("buscar() sin texto retorna página de todos los pacientes")
    void buscar_sinTexto_retornaTodos() {
        PageRequest pageable = PageRequest.of(0, 10);
        Page<Paciente> page = new PageImpl<>(List.of(pacienteExistente));
        when(pacienteRepository.findAll(pageable)).thenReturn(page);

        Page<Paciente> result = pacienteService.buscar(null, pageable);

        assertThat(result.getContent()).hasSize(1);
        verify(pacienteRepository).findAll(pageable);
        verify(pacienteRepository, never()).buscarPorTexto(any(), any());
    }

    @Test
    @DisplayName("buscar() con texto delega a buscarPorTexto()")
    void buscar_conTexto_delegaABuscarPorTexto() {
        PageRequest pageable = PageRequest.of(0, 10);
        Page<Paciente> page = new PageImpl<>(List.of(pacienteExistente));
        when(pacienteRepository.buscarPorTexto("Juan", pageable)).thenReturn(page);

        Page<Paciente> result = pacienteService.buscar("Juan", pageable);

        assertThat(result.getContent()).hasSize(1);
        verify(pacienteRepository).buscarPorTexto("Juan", pageable);
    }
}
