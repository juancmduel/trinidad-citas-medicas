package com.trinidad.citas.service;

import com.trinidad.citas.dto.KpiDashboardDTO;
import com.trinidad.citas.model.EstadoCita;
import com.trinidad.citas.repository.CitaRepository;
import com.trinidad.citas.repository.EspecialidadRepository;
import com.trinidad.citas.repository.MedicoRepository;
import com.trinidad.citas.repository.PacienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

/**
 * Servicio liviano para obtener KPIs del Dashboard.
 * Separado de ReporteService para no cargar dependencias pesadas (OpenPDF, Apache POI)
 * en el controlador de inicio.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardService {

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
