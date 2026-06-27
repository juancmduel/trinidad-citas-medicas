package com.trinidad.citas.repository;

import com.trinidad.citas.model.Cita;
import com.trinidad.citas.model.EstadoCita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Long> {

    List<Cita> findByPaciente_IdPacienteOrderByFechaCitaDescHoraInicioDesc(Long idPaciente);

    List<Cita> findByMedico_IdMedicoAndFechaCitaOrderByHoraInicioAsc(Long idMedico, LocalDate fecha);

    List<Cita> findByMedico_IdMedicoAndFechaCita(Long idMedico, LocalDate fecha);

    List<Cita> findByFechaCitaOrderByHoraInicioAsc(LocalDate fecha);

    List<Cita> findByPaciente_IdPacienteAndFechaCitaOrderByHoraInicioAsc(Long idPaciente, LocalDate fecha);

    @Query("SELECT c FROM Cita c JOIN FETCH c.paciente JOIN FETCH c.medico JOIN FETCH c.especialidad WHERE c.fechaCita = :fecha ORDER BY c.horaInicio")
    List<Cita> findByFechaConRelaciones(@Param("fecha") LocalDate fecha);

    @Query("SELECT c FROM Cita c JOIN FETCH c.paciente JOIN FETCH c.medico JOIN FETCH c.especialidad ORDER BY c.fechaCita DESC, c.horaInicio")
    List<Cita> findAllConRelaciones();

    Optional<Cita> findByMedico_IdMedicoAndFechaCitaAndHoraInicio(Long idMedico, LocalDate fecha, String horaInicio);

    List<Cita> findByMedico_IdMedicoAndEstadoOrderByFechaCitaAscHoraInicioAsc(Long idMedico, EstadoCita estado);

    List<Cita> findByEstado(EstadoCita estado);

    @Query("SELECT c FROM Cita c WHERE c.paciente.idPaciente = :idPaciente " +
           "AND c.medico.idMedico = :idMedico " +
           "AND c.fechaCita >= :fechaDesde " +
           "AND c.estado NOT IN ('CANCELADA', 'NO_ASISTIO')")
    List<Cita> findActivasPacienteMedicoDesde(
        @Param("idPaciente") Long idPaciente,
        @Param("idMedico") Long idMedico,
        @Param("fechaDesde") LocalDate fechaDesde);

    long countByFechaCita(LocalDate fecha);

    long countByFechaCitaAndEstado(LocalDate fecha, EstadoCita estado);

    List<Cita> findByFechaCitaAndEstado(LocalDate fecha, EstadoCita estado);

    @Query("SELECT c.fechaCita, COUNT(c) FROM Cita c " +
           "WHERE c.fechaCita BETWEEN :inicio AND :fin " +
           "GROUP BY c.fechaCita ORDER BY c.fechaCita")
    List<Object[]> countByFechaCitaBetween(@Param("inicio") LocalDate inicio, @Param("fin") LocalDate fin);

    @Query("SELECT c FROM Cita c JOIN FETCH c.paciente JOIN FETCH c.medico JOIN FETCH c.especialidad WHERE c.medico.idMedico = :idMedico ORDER BY c.fechaCita DESC, c.horaInicio")
    List<Cita> findByMedico_IdMedicoConRelaciones(@Param("idMedico") Long idMedico);

    @Query("SELECT c FROM Cita c JOIN FETCH c.paciente JOIN FETCH c.medico JOIN FETCH c.especialidad " +
           "WHERE c.fechaCita BETWEEN :inicio AND :fin ORDER BY c.fechaCita, c.horaInicio")
    List<Cita> findByFechaCitaBetweenConRelaciones(@Param("inicio") LocalDate inicio, @Param("fin") LocalDate fin);

    /** RN-11: citas PROGRAMADA/CONFIRMADA sin check-in cuya hora ya pasó hace > 30 min */
    @Query("SELECT c FROM Cita c WHERE c.estado IN ('PROGRAMADA', 'CONFIRMADA') " +
           "AND c.fechaCita = :hoy " +
           "AND c.horaInicio <= :limiteHora " +
           "AND c.fechaCheckin IS NULL")
    List<Cita> findCitasParaMarcarNoShow(
        @Param("hoy") LocalDate hoy,
        @Param("limiteHora") String limiteHora);
}
