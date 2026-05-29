package com.trinidad.citas.repository;

import com.trinidad.citas.model.HorarioMedico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HorarioMedicoRepository extends JpaRepository<HorarioMedico, Long> {

    List<HorarioMedico> findByMedico_IdMedicoAndActivo(Long idMedico, Integer activo);

    List<HorarioMedico> findByMedico_IdMedicoAndDiaSemanaAndActivo(Long idMedico, Integer diaSemana, Integer activo);

    @Query("SELECT h FROM HorarioMedico h LEFT JOIN FETCH h.medico ORDER BY h.medico.idMedico, h.diaSemana")
    List<HorarioMedico> findAllWithMedico();

    @Query("SELECT h FROM HorarioMedico h LEFT JOIN FETCH h.medico WHERE h.idHorario = ?1")
    Optional<HorarioMedico> findByIdWithMedico(Long id);
}
