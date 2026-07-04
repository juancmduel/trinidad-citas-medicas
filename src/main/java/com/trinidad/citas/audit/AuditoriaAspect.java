package com.trinidad.citas.audit;

import com.trinidad.citas.model.AuditoriaLog;
import com.trinidad.citas.service.AuditoriaLogService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * Aspecto que intercepta todos los metodos anotados con {@link Auditable}
 * y registra la operacion en {@code AUDITORIA_LOG}.
 * <p>
 * Captura automaticamente:
 * <ul>
 *   <li>Usuario autenticado (desde SecurityContext)</li>
 *   <li>Direccion IP (desde HttpServletRequest)</li>
 *   <li>Nombre de la entidad y accion (desde la anotacion)</li>
 *   <li>ID de la entidad afectada (desde el valor de retorno o argumentos)</li>
 *   <li>Detalle adicional y duracion de la operacion</li>
 *   <li>Errores si la operacion lanza una excepcion</li>
 * </ul>
 */
@Aspect
@Component
@RequiredArgsConstructor
public class AuditoriaAspect {

    private static final Logger log = LoggerFactory.getLogger(AuditoriaAspect.class);

    private final AuditoriaLogService auditoriaLogService;

    /**
     * Intercepta la ejecucion del metodo, la ejecuta y registra el resultado.
     */
    @Around("@annotation(auditable)")
    public Object auditar(ProceedingJoinPoint pjp, Auditable auditable) throws Throwable {
        long start = System.nanoTime();
        String errorMsg = null;
        Object result = null;

        try {
            result = pjp.proceed();
            return result;
        } catch (Throwable t) {
            errorMsg = t.getClass().getSimpleName() + ": " + t.getMessage();
            throw t;
        } finally {
            try {
                long durationMs = (System.nanoTime() - start) / 1_000_000;
                registrar(pjp, auditable, result, errorMsg, durationMs);
            } catch (Exception e) {
                log.warn("Error al registrar auditoria para {}.{}: {}",
                        pjp.getSignature().getDeclaringTypeName(),
                        pjp.getSignature().getName(),
                        e.getMessage());
            }
        }
    }

    /* ── Métodos privados ── */

    private void registrar(ProceedingJoinPoint pjp, Auditable auditable,
                           Object result, String errorMsg, long durationMs) {
        AuditoriaLog al = new AuditoriaLog();

        // Usuario autenticado (puede ser anónimo si es una tarea programada)
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()
                && !"anonymousUser".equals(auth.getPrincipal())) {
            al.setUsername(auth.getName());
            // UserPrincipal trae el ID; si no es una instancia de este, lo dejamos null
            if (auth.getPrincipal() instanceof com.trinidad.citas.security.UserPrincipal up) {
                al.setIdUsuario(up.getId());
            }
        }

        al.setAccion(auditable.accion());
        al.setEntidad(auditable.entidad());
        al.setIdEntidad(extraerIdEntidad(pjp, result));

        // Detalle autogenerado si no se especificó uno en la anotación
        String detalle = auditable.detalle();
        if (detalle.isEmpty()) {
            detalle = String.format("%s %s [%s.%s]",
                    auditable.accion(), auditable.entidad().toLowerCase(),
                    pjp.getTarget().getClass().getSimpleName(),
                    pjp.getSignature().getName());
        }
        if (errorMsg != null) {
            detalle += " | ERROR: " + errorMsg;
        } else {
            detalle += String.format(" | OK (%d ms)", durationMs);
        }
        // Truncar a 2000 caracteres (limite de la columna)
        if (detalle.length() > 2000) {
            detalle = detalle.substring(0, 1997) + "...";
        }
        al.setDetalle(detalle);

        al.setIpOrigen(obtenerIp());
        al.setFechaHora(LocalDateTime.now());

        auditoriaLogService.guardar(al);
    }

    /**
     * Intenta extraer un identificador de la entidad afectada por la operación.
     *
     * El orden de precedencia es:
     * <ol>
     *   <li>Valor de retorno si tiene {@code getId()}</li>
     *   <li>Valor de retorno si tiene algún método {@code *Id()} que devuelva Long/Integer</li>
     *   <li>Primer argumento del tipo {@code Long} o {@code Integer}</li>
     * </ol>
     */
    private String extraerIdEntidad(ProceedingJoinPoint pjp, Object result) {
        if (result != null) {
            try {
                Method m = result.getClass().getMethod("getId");
                Object id = m.invoke(result);
                if (id != null) return id.toString();
            } catch (NoSuchMethodException ignored) {
                for (Method m : result.getClass().getMethods()) {
                    String name = m.getName();
                    if (name.endsWith("Id") && !"getId".equals(name)
                            && m.getParameterCount() == 0) {
                        Class<?> ret = m.getReturnType();
                        if (ret == Long.class || ret == long.class
                                || ret == Integer.class || ret == int.class) {
                            try {
                                Object id = m.invoke(result);
                                if (id != null) return id.toString();
                            } catch (Exception ignored2) {
                                // Algunos getters pueden lanzar excepcion, lo ignoramos
                            }
                        }
                    }
                }
            } catch (Exception ignored) {
                // Falló la reflexión, no tenemos ID
            }
        }

        if (pjp.getArgs() != null) {
            for (Object arg : pjp.getArgs()) {
                if (arg instanceof Long || arg instanceof Integer) {
                    return arg.toString();
                }
            }
        }

        return null;
    }

    /**
     * Obtiene la IP real del cliente, con soporte para proxies inversos
     * mediante el header {@code X-Forwarded-For}.
     */
    private String obtenerIp() {
        try {
            RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
            if (attrs instanceof ServletRequestAttributes sra) {
                HttpServletRequest req = sra.getRequest();
                String ip = req.getHeader("X-Forwarded-For");
                if (ip == null || ip.isBlank() || "unknown".equalsIgnoreCase(ip)) {
                    ip = req.getRemoteAddr();
                } else {
                    ip = ip.split(",")[0].trim();
                }
                return ip;
            }
        } catch (Exception ignored) {
            // Sin request HTTP (tests unitarios, tareas programadas, etc.)
        }
        return null;
    }
}
