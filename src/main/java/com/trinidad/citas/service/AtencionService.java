package com.trinidad.citas.service;

import com.trinidad.citas.audit.Auditable;
import com.trinidad.citas.dto.AtencionDTO;
import com.trinidad.citas.exception.BusinessException;
import com.trinidad.citas.exception.ResourceNotFoundException;
import com.trinidad.citas.model.Atencion;
import com.trinidad.citas.model.EstadoCita;
import com.trinidad.citas.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AtencionService {

    private final AtencionRepository atencionRepository;
    private final CitaRepository citaRepository;
    private final HistoriaClinicaRepository historiaClinicaRepository;
    private final MedicoRepository medicoRepository;
    private final DiagnosticoCie10Repository diagnosticoCie10Repository;
    private final PagoRepository pagoRepository;

    public AtencionDTO toDTO(Atencion a) {
        AtencionDTO dto = new AtencionDTO();
        dto.setIdAtencion(a.getIdAtencion());
        dto.setIdCita(a.getCita().getIdCita());
        dto.setIdHistoria(a.getHistoria().getIdHistoria());
        dto.setIdMedico(a.getMedico().getIdMedico());
        dto.setFechaAtencion(a.getFechaAtencion());
        dto.setMotivoConsulta(a.getMotivoConsulta());
        dto.setAnamnesis(a.getAnamnesis());
        dto.setExamenFisico(a.getExamenFisico());
        dto.setDiagnosticoCie10Codigo(a.getDiagnosticoCie10() != null ? a.getDiagnosticoCie10().getCodigo() : null);
        dto.setDiagnosticoDesc(a.getDiagnosticoDesc());
        dto.setTratamiento(a.getTratamiento());
        dto.setObservaciones(a.getObservaciones());
        dto.setPresionArterial(a.getPresionArterial());
        dto.setFrecuenciaCardiaca(a.getFrecuenciaCardiaca());
        dto.setTemperatura(a.getTemperatura());
        dto.setPesoKg(a.getPesoKg());
        dto.setTallaCm(a.getTallaCm());
        if (a.getMedico() != null) {
            dto.setNombreMedicoCompleto(a.getMedico().getNombreCompleto());
        }
        if (a.getCita() != null && a.getCita().getPaciente() != null) {
            dto.setNombrePacienteCompleto(a.getCita().getPaciente().getNombreCompleto());
        }
        return dto;
    }

    @Transactional(readOnly = true)
    public List<Atencion> listarEntidadesConRelaciones() {
        return atencionRepository.findAllWithRelations();
    }

    @Transactional(readOnly = true)
    public List<AtencionDTO> listarTodos() {
        return atencionRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AtencionDTO obtenerPorId(Long id) {
        return toDTO(atencionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Atencion", id)));
    }

    @Transactional(readOnly = true)
    public Atencion obtenerEntidadConRelaciones(Long id) {
        return atencionRepository.findByIdWithRelations(id)
                .orElseThrow(() -> new ResourceNotFoundException("Atencion", id));
    }

    @Transactional(readOnly = true)
    public List<AtencionDTO> listarPorHistoria(Long idHistoria) {
        return atencionRepository.findByHistoria_IdHistoriaOrderByFechaAtencionDesc(idHistoria)
                .stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public AtencionDTO obtenerPorCita(Long idCita) {
        return atencionRepository.findByCita_IdCita(idCita)
                .map(this::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Atencion para cita", idCita));
    }

    @Auditable(entidad = "ATENCION", accion = "CREAR")
    public AtencionDTO crear(AtencionDTO dto) {
        com.trinidad.citas.model.Cita cita = citaRepository.findById(dto.getIdCita())
                .orElseThrow(() -> new ResourceNotFoundException("Cita", dto.getIdCita()));
        if (cita.getEstado() != EstadoCita.EN_ATENCION) {
            throw new BusinessException("La atención solo puede registrarse cuando la cita está EN_ATENCION");
        }
        com.trinidad.citas.model.Pago pago = pagoRepository.findByCita_IdCita(dto.getIdCita())
                .orElseThrow(() -> new BusinessException("No se encontró un pago registrado para esta cita"));
        if (!"PAGADO".equals(pago.getEstado())) {
            throw new BusinessException("El pago de esta cita aún no está confirmado (estado actual: " + pago.getEstado() + ")");
        }
        Atencion a = new Atencion();
        a.setCita(cita);
        a.setHistoria(historiaClinicaRepository.findById(dto.getIdHistoria())
                .orElseThrow(() -> new ResourceNotFoundException("HistoriaClinica", dto.getIdHistoria())));
        a.setMedico(medicoRepository.findById(dto.getIdMedico())
                .orElseThrow(() -> new ResourceNotFoundException("Medico", dto.getIdMedico())));
        a.setFechaAtencion(dto.getFechaAtencion() != null ? dto.getFechaAtencion() : LocalDateTime.now());
        a.setMotivoConsulta(dto.getMotivoConsulta());
        a.setAnamnesis(dto.getAnamnesis());
        a.setExamenFisico(dto.getExamenFisico());
        if (dto.getDiagnosticoCie10Codigo() != null)
            a.setDiagnosticoCie10(diagnosticoCie10Repository.findByCodigo(dto.getDiagnosticoCie10Codigo()).orElse(null));
        a.setDiagnosticoDesc(dto.getDiagnosticoDesc());
        a.setTratamiento(dto.getTratamiento());
        a.setObservaciones(dto.getObservaciones());
        a.setPresionArterial(dto.getPresionArterial());
        a.setFrecuenciaCardiaca(dto.getFrecuenciaCardiaca());
        a.setTemperatura(dto.getTemperatura());
        a.setPesoKg(dto.getPesoKg());
        a.setTallaCm(dto.getTallaCm());
        AtencionDTO saved = toDTO(atencionRepository.save(a));
        cita.setEstado(EstadoCita.ATENDIDA);
        citaRepository.save(cita);
        return saved;
    }

    /**
     * Eliminación lógica: desactiva la atención (activo=0) en lugar de borrarla.
     * Preserva la integridad de históricos clínicos y auditoría.
     */
    @Auditable(entidad = "ATENCION", accion = "ELIMINAR")
    public void eliminar(Long id) {
        Atencion a = atencionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Atencion", id));
        a.setActivo(0);
        atencionRepository.save(a);
    }
}
