package com.trinidad.citas.repository;

import com.trinidad.citas.model.Medico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MedicoRepository extends JpaRepository<Medico, Long> {

    Optional<Medico> findByCmp(String cmp);

    Optional<Medico> findByDni(String dni);

    Optional<Medico> findByUsuario_Username(String username);

    List<Medico> findByEspecialidad_IdEspecialidadAndActivo(Long idEspecialidad, Integer activo);

    @Query("SELECT m FROM Medico m JOIN FETCH m.especialidad WHERE m.activo = :activo ORDER BY m.apellidoPaterno ASC")
    List<Medico> findByActivoOrderByApellidoPaternoAsc(Integer activo);

    @Query("SELECT m FROM Medico m JOIN FETCH m.especialidad ORDER BY m.apellidoPaterno ASC")
    List<Medico> findAllWithEspecialidad();

    boolean existsByCmp(String cmp);

    boolean existsByDni(String dni);

    @Query("SELECT m FROM Medico m LEFT JOIN FETCH m.especialidad WHERE m.idMedico = ?1")
    Optional<Medico> findByIdWithRelations(Long id);
}
