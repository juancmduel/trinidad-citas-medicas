package com.trinidad.citas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Aquí arranca todo. Trinidad Citas Médicas.
 *
 * Un sistema hecho para que los pacientes puedan agendar citas
 * sin tener que llamar por teléfono y esperar 20 minutos en espera.
 * También ayuda al personal de la clínica a organizar mejor el día a día.
 *
 * Cosas que hace:
 *  - Registro de pacientes, médicos, especialidades
 *  - Agendamiento de citas online con horarios reales
 *  - Recordatorios automáticos por correo (para que nadie olvide su cita)
 *  - Triaje, atención médica, recetas, órdenes de examen
 *  - Reportes y dashboards para la gerencia
 *  - Y un montón de cositas más que fuimos agregando sobre la marcha
 *
 * Arranca con el perfil sqlserver por defecto, pero si no tienes SQL Server
 * puedes cambiarlo a 'dev' (H2) u 'oracle' en application.properties.
 */
@SpringBootApplication
@EnableScheduling
public class TrinidadCitasApplication {

    /**
     * Punto de entrada. Como el "mis-spiders" de Spiderman pero con Spring Boot.
     */
    public static void main(String[] args) {
        SpringApplication.run(TrinidadCitasApplication.class, args);
    }
}
