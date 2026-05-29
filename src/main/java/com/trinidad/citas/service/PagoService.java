package com.trinidad.citas.service;

import com.trinidad.citas.dto.PagoDTO;
import com.trinidad.citas.exception.ResourceNotFoundException;
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
        Pago p = new Pago();
        p.setCita(citaRepository.findById(dto.getIdCita())
                .orElseThrow(() -> new ResourceNotFoundException("Cita", dto.getIdCita())));
        p.setMonto(dto.getMonto());
        p.setMetodoPago(dto.getMetodoPago());
        p.setEstado(dto.getEstado() != null ? dto.getEstado() : "PENDIENTE");
        p.setNroComprobante(dto.getNroComprobante());
        p.setTipoComprobante(dto.getTipoComprobante());
        p.setFechaPago(dto.getFechaPago());
        return toDTO(pagoRepository.save(p));
    }

    public PagoDTO cambiarEstado(Long id, String estado) {
        Pago p = pagoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pago", id));
        p.setEstado(estado);
        return toDTO(pagoRepository.save(p));
    }

    public void eliminar(Long id) {
        pagoRepository.deleteById(id);
    }
}
