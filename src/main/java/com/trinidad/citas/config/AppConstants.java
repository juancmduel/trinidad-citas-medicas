package com.trinidad.citas.config;

/**
 * Constantes de aplicación centralizadas.
 * Todas las "magic numbers" y strings repetidos deben definirse aquí
 * para facilitar mantenimiento y evitar duplicación.
 */
public final class AppConstants {

    private AppConstants() { throw new UnsupportedOperationException("Clase de constantes"); }

    // ── Seguridad / Login ─────────────────────────────────────────
    /** Máximo de intentos fallidos antes de bloquear la cuenta */
    public static final int MAX_INTENTOS_FALLIDOS = 5;

    /** Ventana de minutos para contar intentos fallidos */
    public static final int VENTANA_INTENTOS_MINUTOS = 30;

    // ── Auditoría ─────────────────────────────────────────────────
    /** Longitud máxima del campo DETALLE en AUDITORIA_LOG */
    public static final int AUDITORIA_DETALLE_MAX = 2000;

    // ── JWT ───────────────────────────────────────────────────────
    /** Bytes mínimos requeridos para HMAC-SHA256 */
    public static final int JWT_SECRET_MIN_BYTES = 32;

    // ── Paginación ────────────────────────────────────────────────
    public static final int PAGE_SIZE_DEFAULT = 20;
    public static final int PAGE_SIZE_MAX = 100;

    // ── Negocio ───────────────────────────────────────────────────
    /** Días mínimos entre citas con el mismo médico-paciente */
    public static final int DIAS_MIN_ENTRE_CITAS_MISMO_MEDICO = 7;

    /** Minutos de tolerancia para check-in antes de marcar NO_ASISTIO */
    public static final int MINUTOS_TOLERANCIA_CHECKIN = 30;
}
