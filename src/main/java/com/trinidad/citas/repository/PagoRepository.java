package com.trinidad.citas.repository;

import com.trinidad.citas.model.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {
    Optional<Pago> findByCita_IdCita(Long idCita);
    List<Pago> findByEstado(String estado);
}
