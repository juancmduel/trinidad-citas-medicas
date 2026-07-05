package com.trinidad.citas.repository;

import com.trinidad.citas.model.Especialidad;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EspecialidadRepository extends JpaRepository<Especialidad, Long> {

    Optional<Especialidad> findByNombre(String nombre);

    List<Especialidad> findByActivoOrderByNombreAsc(Integer activo);
}
