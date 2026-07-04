package com.trinidad.citas.audit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marca un metodo de servicio para que sea auditado automaticamente
 * por {@link AuditoriaAspect}.
 * <p>
 * Uso:
 * <pre>
 * &#64;Auditable(entidad = "CITA", accion = "CREAR")
 * public CitaDTO agendar(CitaDTO dto) { ... }
 * </pre>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Auditable {

    /** Nombre de la entidad sobre la que se actua (ej: "CITA", "PACIENTE"). */
    String entidad();

    /** Accion realizada (ej: "CREAR", "ACTUALIZAR", "ELIMINAR", "CANCELAR"). */
    String accion();

    /** Descripcion adicional (opcional). Si se omite se genera automaticamente. */
    String detalle() default "";
}
