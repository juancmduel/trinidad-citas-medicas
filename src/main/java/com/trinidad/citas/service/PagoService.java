package com.trinidad.citas.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trinidad.citas.audit.Auditable;
import com.trinidad.citas.dto.PagoDTO;
import com.trinidad.citas.exception.BusinessException;
import com.trinidad.citas.exception.ResourceNotFoundException;
import com.trinidad.citas.model.Cita;
import com.trinidad.citas.model.EstadoCita;
import com.trinidad.citas.model.Pago;
import com.trinidad.citas.repository.CitaRepository;
import com.trinidad.citas.repository.PagoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class PagoService {

    private static final Logger log = LoggerFactory.getLogger(PagoService.class);

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

    @Auditable(entidad = "PAGO", accion = "CREAR")
    public PagoDTO crear(PagoDTO dto) {
        PagoDTO saved = crearPagoBase(dto);
        // Confirmar la cita si el pago se registró como PAGADO
        Cita cita = citaRepository.findById(dto.getIdCita())
                .orElseThrow(() -> new ResourceNotFoundException("Cita", dto.getIdCita()));
        if ("PAGADO".equals(saved.getEstado()) && cita.getEstado() == EstadoCita.PROGRAMADA) {
            citaService.confirmarPago(dto.getIdCita());
        }
        return saved;
    }

    /**
     * Crea un pago SIN confirmar la cita automáticamente.
     * Útil para flujos donde la confirmación se hace en un paso posterior.
     */
    public PagoDTO crearPagoSinConfirmar(PagoDTO dto) {
        return crearPagoBase(dto);
    }

    /**
     * Lógica base compartida para crear un pago.
     * Valida datos, calcula montos, genera comprobante y persiste.
     */
    private PagoDTO crearPagoBase(PagoDTO dto) {
        if (pagoRepository.findByCita_IdCita(dto.getIdCita()).isPresent()) {
            throw new BusinessException("Esta cita ya tiene un pago registrado");
        }
        Cita cita = citaRepository.findById(dto.getIdCita())
                .orElseThrow(() -> new ResourceNotFoundException("Cita", dto.getIdCita()));
        if (cita.getEstado() != EstadoCita.PROGRAMADA && cita.getEstado() != EstadoCita.CONFIRMADA) {
            throw new BusinessException("El pago solo puede registrarse en citas PROGRAMADA o CONFIRMADA");
        }

        BigDecimal monto = dto.getMonto();
        if (monto == null || monto.compareTo(BigDecimal.ZERO) <= 0) {
            if (cita.getEspecialidad() != null && cita.getEspecialidad().getPrecioConsulta() != null) {
                monto = cita.getEspecialidad().getPrecioConsulta();
            } else {
                throw new BusinessException("Debe ingresar un monto válido para el pago");
            }
        }

        String nroComprobante = dto.getNroComprobante();
        String tipoComprobante = dto.getTipoComprobante();
        if (tipoComprobante != null && !tipoComprobante.isBlank() && (nroComprobante == null || nroComprobante.isBlank())) {
            nroComprobante = generarNroComprobante(tipoComprobante);
        }

        Pago p = new Pago();
        p.setCita(cita);
        p.setMonto(monto);
        p.setMetodoPago(dto.getMetodoPago());
        p.setEstado(dto.getEstado() != null ? dto.getEstado() : "PENDIENTE");
        p.setNroComprobante(nroComprobante);
        p.setTipoComprobante(tipoComprobante);
        if ("PAGADO".equals(p.getEstado())) {
            p.setFechaPago(dto.getFechaPago() != null ? dto.getFechaPago() : LocalDateTime.now());
        }
        return toDTO(pagoRepository.save(p));
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
            p.setFechaPago(dto.getFechaPago() != null ? dto.getFechaPago() : LocalDateTime.now());
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
        if ("PAGADO".equals(estado) && !"PAGADO".equals(estadoAnterior)) {
            p.setFechaPago(LocalDateTime.now());
        }
        PagoDTO saved = toDTO(pagoRepository.save(p));
        if ("PAGADO".equals(estado) && !"PAGADO".equals(estadoAnterior)) {
            Cita c = p.getCita();
            if (c.getEstado() == EstadoCita.PROGRAMADA) {
                citaService.confirmarPago(c.getIdCita());
            }
        }
        return saved;
    }

    /**
     * Anulación/eliminación lógica: desactiva el pago (activo=0) en lugar de borrarlo.
     * Preserva el histórico financiero y la auditoría.
     */
    @Auditable(entidad = "PAGO", accion = "ANULAR")
    public void eliminar(Long id) {
        Pago p = pagoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pago", id));
        p.setActivo(0);
        pagoRepository.save(p);
    }

    @Transactional(readOnly = true)
    public synchronized String generarNroComprobante(String tipo) {
        String prefix = switch (tipo) {
            case "BOLETA" -> "B001-";
            case "FACTURA" -> "F001-";
            case "RECIBO" -> "R001-";
            default -> throw new BusinessException("Tipo de comprobante no válido: " + tipo + ". Use: BOLETA, FACTURA o RECIBO");
        };
        String maxNro = pagoRepository.findMaxNroComprobanteByPrefix(prefix + "%");
        int next = 1;
        if (maxNro != null && maxNro.startsWith(prefix)) {
            try {
                next = Integer.parseInt(maxNro.substring(prefix.length())) + 1;
            } catch (NumberFormatException e) {
                log.warn("No se pudo parsear el correlativo del comprobante '{}': {}", maxNro, e.getMessage());
            }
        }
        return prefix + String.format("%06d", next);
    }
}