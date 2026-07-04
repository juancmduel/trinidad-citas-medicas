package com.trinidad.citas.service;

import com.trinidad.citas.audit.Auditable;
import com.trinidad.citas.dto.AdmisionPacienteDTO;
import com.trinidad.citas.dto.PagoDTO;
import com.trinidad.citas.exception.BusinessException;
import com.trinidad.citas.exception.ResourceNotFoundException;
import com.trinidad.citas.model.Cita;
import com.trinidad.citas.model.EstadoCita;
import com.trinidad.citas.model.Paciente;
import com.trinidad.citas.repository.CitaRepository;
import com.trinidad.citas.repository.PacienteRepository;
import com.trinidad.citas.repository.PagoRepository;
import com.trinidad.citas.repository.TriajeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AdmisionService {

    private final PacienteRepository pacienteRepository;
    private final CitaRepository citaRepository;
    private final PagoRepository pagoRepository;
    private final TriajeRepository triajeRepository;
    private final PagoService pagoService;
    private final CitaService citaService;

    public Paciente buscarPaciente(String dni) {
        return pacienteRepository.findByDni(dni)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente con DNI " + dni));
    }

    public List<Cita> obtenerCitasDelDia(Paciente paciente) {
        return citaRepository.findByPaciente_IdPacienteAndFechaCitaOrderByHoraInicioAsc(
                paciente.getIdPaciente(), LocalDate.now());
    }

    @Auditable(entidad = "ADMISION", accion = "PROCESAR")
    @Transactional
    public Cita procesarAdmision(Long idCita, String metodoPago, String tipoComprobante) {
        Cita cita = citaRepository.findById(idCita)
                .orElseThrow(() -> new ResourceNotFoundException("Cita", idCita));

        if (cita.getEstado() == EstadoCita.EN_TRIAGE || cita.getEstado() == EstadoCita.EN_ATENCION) {
            return cita;
        }

        boolean tienePago = pagoRepository.findByCita_IdCita(idCita).isPresent();

        if (cita.getEstado() == EstadoCita.PROGRAMADA) {
            if (!tienePago) {
                PagoDTO pagoDTO = new PagoDTO();
                pagoDTO.setIdCita(idCita);
                pagoDTO.setMetodoPago(metodoPago);
                pagoDTO.setEstado("PAGADO");
                pagoDTO.setTipoComprobante(tipoComprobante);
                pagoService.crear(pagoDTO);
            }
            citaService.checkin(idCita);
        } else if (cita.getEstado() == EstadoCita.CONFIRMADA) {
            if (!tienePago) {
                PagoDTO pagoDTO = new PagoDTO();
                pagoDTO.setIdCita(idCita);
                pagoDTO.setMetodoPago(metodoPago);
                pagoDTO.setEstado("PAGADO");
                pagoDTO.setTipoComprobante(tipoComprobante);
                pagoService.crearPagoSinConfirmar(pagoDTO);
            }
            citaService.checkin(idCita);
        }

        return citaRepository.findById(idCita)
                .orElseThrow(() -> new ResourceNotFoundException("Cita", idCita));
    }

    @Transactional(readOnly = true)
    public List<AdmisionPacienteDTO> obtenerPacientesHoy() {
        LocalDate hoy = LocalDate.now();
        return citaRepository.findByFechaConRelaciones(hoy).stream()
                .map(this::toAdmisionPacienteDTO)
                .toList();
    }

    private AdmisionPacienteDTO toAdmisionPacienteDTO(Cita cita) {
        AdmisionPacienteDTO dto = new AdmisionPacienteDTO();

        dto.setIdCita(cita.getIdCita());
        dto.setHoraInicio(cita.getHoraInicio());
        dto.setHoraFin(cita.getHoraFin());
        dto.setEstado(cita.getEstado().name());
        dto.setNumeroTurno(cita.getNumeroTurno());

        if (cita.getPaciente() != null) {
            dto.setIdPaciente(cita.getPaciente().getIdPaciente());
            dto.setNombrePaciente(cita.getPaciente().getNombreCompleto());
            dto.setDniPaciente(cita.getPaciente().getDni());
            dto.setTelefonoPaciente(cita.getPaciente().getTelefono());
        }

        if (cita.getMedico() != null) {
            dto.setIdMedico(cita.getMedico().getIdMedico());
            dto.setNombreMedico(cita.getMedico().getNombreCompleto());
            dto.setConsultorio(cita.getMedico().getConsultorio());
        }

        if (cita.getEspecialidad() != null) {
            dto.setIdEspecialidad(cita.getEspecialidad().getIdEspecialidad());
            dto.setNombreEspecialidad(cita.getEspecialidad().getNombre());
            dto.setPrecioConsulta(cita.getEspecialidad().getPrecioConsulta());
        }

        pagoRepository.findByCita_IdCita(cita.getIdCita()).ifPresent(pago -> {
            dto.setIdPago(pago.getIdPago());
            dto.setMontoPagado(pago.getMonto());
            dto.setMetodoPago(pago.getMetodoPago());
            dto.setEstadoPago(pago.getEstado());
            dto.setTipoComprobante(pago.getTipoComprobante());
        });

        triajeRepository.findByCita_IdCita(cita.getIdCita()).ifPresent(triaje -> {
            dto.setIdTriaje(triaje.getIdTriaje());
            dto.setPresionArterial(triaje.getPresionArterial());
            dto.setFrecuenciaCardiaca(triaje.getFrecuenciaCardiaca());
            dto.setTemperatura(triaje.getTemperatura());
            dto.setSaturacionO2(triaje.getSaturacionO2());
            dto.setNivelUrgencia(triaje.getNivelUrgencia());
        });

        return dto;
    }
}
