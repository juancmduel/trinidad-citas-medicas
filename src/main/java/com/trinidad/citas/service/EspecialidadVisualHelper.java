package com.trinidad.citas.service;

import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Mapea especialidades médicas a iconos (Bootstrap Icons) y colores acento.
 * Así cada tarjeta de especialidad tiene un ícono y color único.
 * Si una especialidad no está en el mapa, usa valores por defecto.
 */
@Component
public class EspecialidadVisualHelper {

    private static final Map<String, IconoColor> MAP = Map.ofEntries(
        Map.entry("medicina general",     new IconoColor("bi bi-person-heart",     "#10b981", "#d1fae5")),
        Map.entry("pediatria",            new IconoColor("bi bi-balloon-heart",    "#3b82f6", "#dbeafe")),
        Map.entry("ginecologia",          new IconoColor("bi bi-gender-female",    "#ec4899", "#fce7f3")),
        Map.entry("cardiologia",          new IconoColor("bi bi-heart-pulse",      "#ef4444", "#fee2e2")),
        Map.entry("traumatologia",        new IconoColor("bi bi-activity",         "#f59e0b", "#fef3c7")),
        Map.entry("dermatologia",         new IconoColor("bi bi-eyedropper",       "#8b5cf6", "#ede9fe")),
        Map.entry("oftalmologia",         new IconoColor("bi bi-eye",              "#06b6d4", "#cffafe")),
        Map.entry("otorrinolaringologia", new IconoColor("bi bi-ear",              "#14b8a6", "#ccfbf1")),
        Map.entry("neurologia",           new IconoColor("bi bi-cpu",              "#6366f1", "#e0e7ff")),
        Map.entry("psiquiatria",          new IconoColor("bi bi-chat-quote",       "#a855f7", "#f3e8ff")),
        Map.entry("urologia",             new IconoColor("bi bi-droplet-half",     "#0ea5e9", "#e0f2fe")),
        Map.entry("endocrinologia",       new IconoColor("bi bi-graph-up-arrow",   "#f97316", "#ffedd5")),
        Map.entry("gastroenterologia",    new IconoColor("bi bi-emoji-sunglasses", "#84cc16", "#ecfccb")),
        Map.entry("reumatologia",         new IconoColor("bi bi-person-walking",   "#e11d48", "#ffe4e6")),
        Map.entry("odontologia",          new IconoColor("bi bi-emoji-smile",      "#0d9488", "#ccfbf1"))
    );

    private static final IconoColor DEFAULT = new IconoColor("bi bi-stethoscope", "#6b7280", "#f3f4f6");

    public IconoColor get(String nombreEspecialidad) {
        if (nombreEspecialidad == null) return DEFAULT;
        return MAP.getOrDefault(nombreEspecialidad.toLowerCase().trim(), DEFAULT);
    }

    public record IconoColor(String icono, String color, String bgLight) {}
}
