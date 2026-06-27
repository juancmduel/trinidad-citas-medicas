package com.trinidad.citas.service;

import com.trinidad.citas.dto.PagoDTO;
import com.trinidad.citas.exception.BusinessException;
import com.trinidad.citas.exception.ResourceNotFoundException;
import com.trinidad.citas.model.Cita;
import com.trinidad.citas.model.EstadoCita;
import com.trinidad.citas.model.Pago;
import com.trinidad.citas.repository.CitaRepository;
import com.trinidad.citas.repository.PagoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PagoService {

    private final PagoRepository pagoRepository;
    private final CitaRepository citaRepository;
    private final CitaService citaService;

    @Transactional(readOnly = true)
    public List<Pago> listarEntidades() {
        return pagoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Pago obtenerEntidad(Long id) {
        return pagoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pago", id));
    }

    public PagoDTO toDTO(Pago p) {
        PagoDTO dto = new PagoDTO();
        dto.setIdPago(p.getIdPago());
        dto.setIdCita(p.getCita().getIdCita());
        dto.setMonto(p.getMonto());
        dto.setMetodoPago(p.getMetodoPago());
        dto.setEstado(p.getEstado());
        dto.setNroComprobante(p.getNroComprobante());
        dto.setTipoComprobante(p.getTipoComprobante());
        dto.setFechaPago(p.getFechaPago());
        dto.setFechaRegistro(p.getFechaRegistro());
        return dto;
    }

    @Transactional(readOnly = true)
    public List<PagoDTO> listarTodos() {
        return pagoRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PagoDTO> listarPorEstado(String estado) {
        return pagoRepository.findByEstado(estado).stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PagoDTO obtenerPorId(Long id) {
        return toDTO(pagoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pago", id)));
    }

    @Transactional(readOnly = true)
    public PagoDTO obtenerPorCita(Long idCita) {
        return pagoRepository.findByCita_IdCita(idCita)
                .map(this::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Pago para cita", idCita));
    }

    public PagoDTO crear(PagoDTO dto) {
        if (pagoRepository.findByCita_IdCita(dto.getIdCita()).isPresent()) {
            throw new BusinessException("La cita ya tiene un pago registrado");
        }
        Cita cita = citaRepository.findById(dto.getIdCita())
                .orElseThrow(() -> new ResourceNotFoundException("Cita", dto.getIdCita()));
        if (cita.getEstado() != EstadoCita.PROGRAMADA && cita.getEstado() != EstadoCita.CONFIRMADA) {
            throw new BusinessException("Solo se puede registrar pago en citas PROGRAMADA o CONFIRMADA");
        }
        Pago p = new Pago();
        p.setCita(cita);
        p.setMonto(dto.getMonto());
        p.setMetodoPago(dto.getMetodoPago());
        p.setEstado(dto.getEstado() != null ? dto.getEstado() : "PENDIENTE");
        p.setNroComprobante(dto.getNroComprobante());
        p.setTipoComprobante(dto.getTipoComprobante());
        p.setFechaPago(dto.getFechaPago());
        PagoDTO saved = toDTO(pagoRepository.save(p));
        if ("PAGADO".equals(p.getEstado())) {
            citaService.confirmarPago(dto.getIdCita());
        }
        return saved;
    }

    public PagoDTO actualizar(Long id, PagoDTO dto) {
        Pago p = pagoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pago", id));
        p.setMonto(dto.getMonto());
        p.setMetodoPago(dto.getMetodoPago());
        p.setNroComprobante(dto.getNroComprobante());
        p.setTipoComprobante(dto.getTipoComprobante());
        String estadoAnterior = p.getEstado();
        if (dto.getEstado() != null) p.setEstado(dto.getEstado());
        if ("PAGADO".equals(dto.getEstado())) {
            p.setFechaPago(dto.getFechaPago() != null ? dto.getFechaPago() : java.time.LocalDateTime.now());
        }
        PagoDTO saved = toDTO(pagoRepository.save(p));
        if ("PAGADO".equals(p.getEstado()) && !"PAGADO".equals(estadoAnterior)) {
            Cita c = p.getCita();
            if (c.getEstado() == EstadoCita.PROGRAMADA) {
                citaService.confirmarPago(c.getIdCita());
            }
        }
        return saved;
    }

    public PagoDTO cambiarEstado(Long id, String estado) {
        Pago p = pagoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pago", id));
        String estadoAnterior = p.getEstado();
        p.setEstado(estado);
        PagoDTO saved = toDTO(pagoRepository.save(p));
        if ("PAGADO".equals(estado) && !"PAGADO".equals(estadoAnterior)) {
            Cita c = p.getCita();
            if (c.getEstado() == EstadoCita.PROGRAMADA) {
                citaService.confirmarPago(c.getIdCita());
            }
        }
        return saved;
    }

    public void eliminar(Long id) {
        pagoRepository.deleteById(id);
    }
}
