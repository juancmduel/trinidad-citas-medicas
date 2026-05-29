package com.trinidad.citas.service;

import com.trinidad.citas.dto.KpiDashboardDTO;
import com.trinidad.citas.model.EstadoCita;
import com.trinidad.citas.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReporteService {

    private final PacienteRepository pacienteRepository;
    private final MedicoRepository medicoRepository;
    private final CitaRepository citaRepository;
    private final EspecialidadRepository especialidadRepository;

    public KpiDashboardDTO obtenerKpisDashboard() {
        LocalDate hoy = LocalDate.now();
        return KpiDashboardDTO.builder()
            .totalPacientes(pacienteRepository.count())
            .totalMedicos(medicoRepository.count())
            .citasHoy(citaRepository.countByFechaCita(hoy))
            .citasAtendidasHoy(citaRepository.countByFechaCitaAndEstado(hoy, EstadoCita.ATENDIDA))
            .citasPendientesHoy(citaRepository.countByFechaCitaAndEstado(hoy, EstadoCita.PROGRAMADA)
                                + citaRepository.countByFechaCitaAndEstado(hoy, EstadoCita.CONFIRMADA))
            .citasCanceladasHoy(citaRepository.countByFechaCitaAndEstado(hoy, EstadoCita.CANCELADA))
            .totalEspecialidades(especialidadRepository.count())
            .build();
    }
}
