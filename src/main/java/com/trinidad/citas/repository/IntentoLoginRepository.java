package com.trinidad.citas.repository;

import com.trinidad.citas.model.IntentoLogin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface IntentoLoginRepository extends JpaRepository<IntentoLogin, Long> {

    List<IntentoLogin> findAllByOrderByFechaHoraDesc();

    List<IntentoLogin> findByUsernameOrderByFechaHoraDesc(String username);

    List<IntentoLogin> findByUsernameAndExitosoAndFechaHoraAfter(String username, Integer exitoso, LocalDateTime fechaHora);

    long countByUsernameAndExitosoAndFechaHoraAfter(String username, Integer exitoso, LocalDateTime fechaHora);
}
