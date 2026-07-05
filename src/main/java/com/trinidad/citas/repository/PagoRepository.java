package com.trinidad.citas.repository;

import com.trinidad.citas.model.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PagoRepository extends JpaRepository<Pago, Long> {
    Optional<Pago> findByCita_IdCita(Long idCita);
    List<Pago> findByEstado(String estado);

    @Query("SELECT p FROM Pago p JOIN FETCH p.cita c JOIN FETCH c.paciente JOIN FETCH c.medico JOIN FETCH c.especialidad WHERE p.fechaPago BETWEEN :desde AND :hasta ORDER BY p.fechaPago")
    List<Pago> findWithCitaByFechaPagoBetween(@Param("desde") LocalDateTime desde, @Param("hasta") LocalDateTime hasta);

    @Query("SELECT COALESCE(SUM(p.monto), 0) FROM Pago p WHERE p.estado = 'PAGADO' AND p.fechaPago BETWEEN :desde AND :hasta")
    java.math.BigDecimal sumMontoByFechaPagoBetween(@Param("desde") LocalDateTime desde, @Param("hasta") LocalDateTime hasta);

    @Query("SELECT MAX(p.nroComprobante) FROM Pago p WHERE p.nroComprobante LIKE :prefix")
    String findMaxNroComprobanteByPrefix(@Param("prefix") String prefix);
}
