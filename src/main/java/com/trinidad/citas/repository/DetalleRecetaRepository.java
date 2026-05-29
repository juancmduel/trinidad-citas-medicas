package com.trinidad.citas.repository;

import com.trinidad.citas.model.DetalleReceta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetalleRecetaRepository extends JpaRepository<DetalleReceta, Long> {
    List<DetalleReceta> findByReceta_IdReceta(Long idReceta);
}
