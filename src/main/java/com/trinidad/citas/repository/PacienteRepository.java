package com.trinidad.citas.repository;

import com.trinidad.citas.model.Paciente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {

    Optional<Paciente> findByDni(String dni);

    Optional<Paciente> findByUsuario_IdUsuario(Long idUsuario);

    boolean existsByDni(String dni);

    @Query("SELECT p FROM Paciente p WHERE " +
           "LOWER(p.nombres) LIKE LOWER(CONCAT('%', :texto, '%')) OR " +
           "LOWER(p.apellidoPaterno) LIKE LOWER(CONCAT('%', :texto, '%')) OR " +
           "LOWER(p.apellidoMaterno) LIKE LOWER(CONCAT('%', :texto, '%')) OR " +
           "p.dni LIKE CONCAT('%', :texto, '%')")
    Page<Paciente> buscarPorTexto(@Param("texto") String texto, Pageable pageable);
}
