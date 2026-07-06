package com.trinidad.citas.config;

/**
 * Aquí vivEN las constantes de la aplicación.
 *
 * En lugar de tener números mágicos repartidos por todo el código
 * (ej: "if (intentos > 5)"), mejor las definimos acá y les ponemos
 * un nombre que explique qué hacen.
 *
 * Si algún día queremos cambiar el número de intentos máximos o
 * los minutos de tolerancia, lo cambiamos aquí y listo.
 */
public final class AppConstants {

    private AppConstants() { throw new UnsupportedOperationException("Clase de constantes"); }

    // ── Seguridad / Login ─────────────────────────────────────────
    /** 
     * Si alguien mete mal la contraseña más de 5 veces en 30 minutos,
     * asumimos que no es el dueño de la cuenta y la bloqueamos.
     */
    public static final int MAX_INTENTOS_FALLIDOS = 5;

    /** Ventana de tiempo (en minutos) para contar los intentos fallidos */
    public static final int VENTANA_INTENTOS_MINUTOS = 30;

    // ── Auditoría ─────────────────────────────────────────────────
    /** Los logs de auditoría no pueden ser infinitos, 2000 caracteres está bien */
    public static final int AUDITORIA_DETALLE_MAX = 2000;

    // ── JWT ───────────────────────────────────────────────────────
    /** Para el JWT necesitamos al menos 32 bytes de clave secreta */
    public static final int JWT_SECRET_MIN_BYTES = 32;

    // ── Paginación ────────────────────────────────────────────────
    public static final int PAGE_SIZE_DEFAULT = 20;
    public static final int PAGE_SIZE_MAX = 100;

    // ── Negocio ───────────────────────────────────────────────────
    /** 
     * Un paciente no puede agendar otra cita con el mismo médico
     * si ya tuvo una hace menos de 7 días. 
     * Así evitamos que acaparen turnos.
     */
    public static final int DIAS_MIN_ENTRE_CITAS_MISMO_MEDICO = 7;

    /**
     * Si el paciente no hace check-in 30 minutos después de la hora
     * de su cita, se marca como NO_ASISTIO y liberamos el cupo.
     */
    public static final int MINUTOS_TOLERANCIA_CHECKIN = 30;
}
