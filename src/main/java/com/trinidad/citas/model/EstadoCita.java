package com.trinidad.citas.model;

/**
 * Los estados por los que pasa una cita médica.
 *
 * Una cita recién creada arranca como PROGRAMADA y va avanzando
 * a medida que el paciente y el personal de la clínica hacen su trabajo:
 *
 *   PROGRAMADA → (pago) → CONFIRMADA → (check-in) → EN_TRIAGE → EN_ATENCION → ATENDIDA
 *
 * Si algo sale mal, puede ir a:
 *   - CANCELADA: el paciente o la clínica cancelaron
 *   - NO_ASISTIO: el paciente nunca llegó
 *   - REPROGRAMADA: se movió para otra fecha
 */
public enum EstadoCita {
    /** Recién creada, esperando confirmación de pago */
    PROGRAMADA,
    /** Pago confirmado, el paciente ya tiene la cita asegurada */
    CONFIRMADA,
    /** El paciente llegó y está siendo evaluado por enfermería */
    EN_TRIAGE,
    /** El médico está atendiendo al paciente en consulta */
    EN_ATENCION,
    /** Atención terminada. El paciente ya fue atendido */
    ATENDIDA,
    /** La cita fue cancelada (por el paciente o por la clínica) */
    CANCELADA,
    /** El paciente no se presentó. Pasaron 30 min después de la hora */
    NO_ASISTIO,
    /** La cita fue reprogramada para otra fecha/hora */
    REPROGRAMADA
}
