package com.trinidad.citas.repository;

import com.trinidad.citas.model.DetalleReceta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DetalleRecetaRepository extends JpaRepository<DetalleReceta, Long> {
    List<DetalleReceta> findByReceta_IdReceta(Long idReceta);
}
